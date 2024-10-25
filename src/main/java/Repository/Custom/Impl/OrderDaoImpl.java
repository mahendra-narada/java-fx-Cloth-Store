package Repository.Custom.Impl;

import Entity.OrderDetailEntity;
import Entity.OrderEntity;
import Entity.UserEntity;
import Repository.Custom.OrderDao;
import javafx.collections.ObservableList;
import model.CartItem;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class OrderDaoImpl implements OrderDao {


    @Override
    public boolean saveOrder(String orderId,String customerName, String customerEmail, List<CartItem> cartItems,String cashier) {
        OrderEntity order = new OrderEntity();
        order.setOrderId(orderId);
        order.setCustomerName(customerName);
        order.setCustomerEmail(customerEmail);
        order.setOrderDate(LocalDate.now());
        order.setOrderTime(LocalTime.now());
        order.setCashier(cashier);

        // Create order details for each cart item
        List<OrderDetailEntity> orderDetails = new ArrayList<>();
        double totalOrderPrice = 0.0;

        for (CartItem cartItem : cartItems) {
            double itemTotalPrice = cartItem.getPrice() * cartItem.getQuantity();
            totalOrderPrice += itemTotalPrice;

            // Create an OrderDetailEntity for each item
            OrderDetailEntity orderDetail = new OrderDetailEntity(cartItem.getItemName(), cartItem.getQuantity(), itemTotalPrice);
            orderDetail.setOrder(order); // Set the parent order

            // Add to the list of order details
            orderDetails.add(orderDetail);
        }

        // Set the total order price and order details to the order
        order.setOrderTotal(totalOrderPrice);
        order.setOrderDetails(orderDetails);

        // Save the order and order details using Hibernate
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.save(order);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }
    public int getLastOrderId() {
        int lastOrderId = 0;

        try (Session session = HibernateUtil.getSession()) {
            // Use HQL (Hibernate Query Language) to get the max orderId from the OrderEntity
            Query<Integer> query = session.createQuery("SELECT MAX(u.orderId) FROM OrderEntity u", Integer.class);
            Integer result = query.uniqueResult();

            if (result != null) {
                lastOrderId = result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lastOrderId;
    }


    @Override
    public List<OrderEntity> getAllOrders() {

        List<OrderEntity> orders = null;
        try (Session session = HibernateUtil.getSession()) {
            // Using HQL to fetch all orders and their details eagerly
            Query<OrderEntity> query = session.createQuery("FROM OrderEntity", OrderEntity.class);
            orders = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public List<OrderDetailEntity> getAllItems() {
        List<OrderDetailEntity> items = null;
        try (Session session = HibernateUtil.getSession()) {
            // Using HQL to fetch all order details
            Query<OrderDetailEntity> query = session.createQuery("FROM OrderDetailEntity", OrderDetailEntity.class);
            items = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<OrderDetailEntity> getOrderDetailsByOrderId(String orderId) {
        List<OrderDetailEntity> orderItems = null;

        try (Session session = HibernateUtil.getSession()) {
            Query<OrderDetailEntity> query = session.createQuery("FROM OrderDetailEntity o WHERE o.order.orderId = :orderId", OrderDetailEntity.class);
            query.setParameter("orderId", orderId);
            orderItems = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return orderItems;
    }

    @Override
    public Double getTodayTotalForUser(String cashier) {
        Double todayTotal = 0.0;

        try (Session session = HibernateUtil.getSession()) {
            // Query to fetch the sum of orderTotal for today and the current user (cashier)
            Query<Double> query = session.createQuery(
                    "SELECT SUM(o.orderTotal) FROM OrderEntity o WHERE o.cashier = :cashier AND o.orderDate = :today",
                    Double.class
            );
            query.setParameter("cashier", cashier);
            query.setParameter("today", LocalDate.now());

            todayTotal = query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return todayTotal != null ? todayTotal : 0.0;
    }

    @Override
    public int getTodayTotalOrdersForUser(String cashier) {
        int totalOrders = 0;
        try (Session session = HibernateUtil.getSession()) {
            // Assuming you have a field 'orderDate' in your OrderEntity and 'cashier' to track the user
            Query<Long> query = session.createQuery("SELECT COUNT(o) FROM OrderEntity o WHERE o.cashier = :cashier AND DATE(o.orderDate) = CURRENT_DATE", Long.class);
            query.setParameter("cashier", cashier);
            totalOrders = query.uniqueResult().intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalOrders;
    }

    @Override
    public Map<String, Double> getDailySales() {
        Map<String, Double> weeklySales = new HashMap<>();

        try (Session session = HibernateUtil.getSession()) {
            // Query to get weekly sales
            String hql = "SELECT DATE_FORMAT(orderDate, '%Y-%u'), SUM(orderTotal) " +
                    "FROM OrderEntity " +
                    "GROUP BY DATE_FORMAT(orderDate, '%Y-%u') " +
                    "ORDER BY DATE_FORMAT(orderDate, '%Y-%u')";

            Query<Object[]> query = session.createQuery(hql, Object[].class);
            List<Object[]> results = query.getResultList();

            for (Object[] result : results) {
                String week = result[0].toString(); // Week in format Year-WeekNumber
                Double totalSales = (Double) result[1];
                weeklySales.put(week, totalSales);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return weeklySales;
    }

    @Override
    public boolean updateOrder(OrderEntity order) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.update(order); // Update the existing order in the database
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Handle exceptions and return false if the update fails
        }
    }

    @Override
    public Map<String, Long> getWeeklyOrderCount() {
        Map<String, Long> dailyOrderCount = new HashMap<>();

        try (Session session = HibernateUtil.getSession()) {
            String hql = "SELECT DATE(orderDate), COUNT(*) " +
                    "FROM OrderEntity " +
                    "WHERE orderDate >= :startOfWeek AND orderDate <= :endOfWeek " +
                    "GROUP BY DATE(orderDate)";

            Query<Object[]> query = session.createQuery(hql, Object[].class);

            // Set the start and end of the current week
            LocalDate today = LocalDate.now();
            LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
            LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);

            query.setParameter("startOfWeek", startOfWeek);
            query.setParameter("endOfWeek", endOfWeek);

            List<Object[]> results = query.getResultList();

            for (Object[] result : results) {
                String date = result[0].toString(); // Date as String
                Long orderCount = (Long) result[1]; // Count of orders
                dailyOrderCount.put(date, orderCount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dailyOrderCount;
    }

    public long getTodayTotalOrdersCount() {
        long ordersCount = 0;

        try (Session session = HibernateUtil.getSession()) {
            String hql = "SELECT COUNT(orderId) FROM OrderEntity WHERE DATE(orderDate) = :today";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("today", java.sql.Date.valueOf(LocalDate.now()));

            ordersCount = query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ordersCount;
    }

    public Double getTotalOrderSum() {
        Double totalSum = 0.0;
        try (Session session = HibernateUtil.getSession()) {
            String hql = "SELECT SUM(orderTotal) FROM OrderEntity WHERE DATE(orderDate) = CURRENT_DATE";
            Query<Double> query = session.createQuery(hql, Double.class);
            totalSum = query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalSum != null ? totalSum : 0.0;
    }

    public String getTopCashierToday() {
        String topCashier = null;
        try (Session session = HibernateUtil.getSession()) {
            String hql = "SELECT cashier, COUNT(*) as orderCount FROM OrderEntity " +
                    "WHERE DATE(orderDate) = CURRENT_DATE " +
                    "GROUP BY cashier " +
                    "ORDER BY orderCount DESC";
            Query<Object[]> query = session.createQuery(hql, Object[].class);
            query.setMaxResults(1);  // Limit to the top result
            Object[] result = query.uniqueResult();

            if (result != null) {
                topCashier = (String) result[0];  // Extract the cashier's name
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return topCashier != null ? topCashier : "No orders today";
    }

    public String getTopItemToday() {
        String topItem = null;
        try (Session session = HibernateUtil.getSession()) {
            String hql = "SELECT od.itemName, SUM(od.itemQty) as totalQuantity FROM OrderEntity o " +
                    "JOIN o.orderDetails od " +
                    "WHERE DATE(o.orderDate) = CURRENT_DATE " +
                    "GROUP BY od.itemName " +
                    "ORDER BY totalQuantity DESC";
            Query<Object[]> query = session.createQuery(hql, Object[].class);
            query.setMaxResults(1);  // Limit to the top result
            Object[] result = query.uniqueResult();

            if (result != null) {
                topItem = (String) result[0];  // Extract the item name
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return topItem != null ? topItem : "No orders today";
    }




}

