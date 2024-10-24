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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
}

