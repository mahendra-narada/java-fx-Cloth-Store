package Service.Custom.Impl;

import Entity.OrderDetailEntity;
import Entity.OrderEntity;
import Repository.Custom.ItemDao;
import Repository.Custom.OrderDao;
import Repository.DaoFactory;
import Service.Custom.OrderService;
import model.CartItem;
import util.DaoType;

import java.util.List;
import java.util.Map;

public class OrderServiceImpl implements OrderService {


    @Override
    public int fetchLastItemId() {
        OrderDao orderDao = DaoFactory.getInstance().getServiceType(DaoType.ORDER);
        int userid= orderDao.getLastOrderId();
        userid=userid+1;
        return userid;
    }


    @Override
    public boolean placeOrder(String orderID,String customerName, String customerEmail, List<CartItem> cartItems,String cashier) {
        OrderDao orderDao = DaoFactory.getInstance().getServiceType(DaoType.ORDER);
        try {
            // Create and save the order
            return orderDao.saveOrder(orderID,customerName, customerEmail, cartItems,cashier);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<OrderEntity> getAllOrders() {
        OrderDao orderDao = DaoFactory.getInstance().getServiceType(DaoType.ORDER);
        return orderDao.getAllOrders();
    }

    @Override
    public List<OrderDetailEntity> getAllItems() {
        OrderDao orderDao = DaoFactory.getInstance().getServiceType(DaoType.ORDER);
        return orderDao.getAllItems();
    }
    public List<OrderDetailEntity> getOrderDetailsByOrderId(String orderId){
        OrderDao orderDao = DaoFactory.getInstance().getServiceType(DaoType.ORDER);
        return orderDao.getOrderDetailsByOrderId(orderId);
    }

    @Override
    public Double getTodayTotalForUser(String cashier) {
        OrderDao orderDao = DaoFactory.getInstance().getServiceType(DaoType.ORDER);
        return orderDao.getTodayTotalForUser(cashier);
    }

    @Override
    public int getTodayTotalOrdersForUser(String cashier) {
        OrderDao orderDao = DaoFactory.getInstance().getServiceType(DaoType.ORDER);
        return orderDao.getTodayTotalOrdersForUser(cashier);
    }

    @Override
    public Map<String, Double> getDailySales() {
        OrderDao orderDao = DaoFactory.getInstance().getServiceType(DaoType.ORDER);
        return orderDao.getDailySales();
    }

    @Override
    public boolean updateOrder(OrderEntity order) {
        OrderDao orderDao = DaoFactory.getInstance().getServiceType(DaoType.ORDER);
        return orderDao.updateOrder(order);
    }

    @Override
    public Map<String, Long> getWeeklyOrderCount() {
        OrderDao orderDao = DaoFactory.getInstance().getServiceType(DaoType.ORDER);
        return orderDao.getWeeklyOrderCount();
    }

    @Override
    public long getTodayTotalOrdersCount() {
        OrderDao orderDao = DaoFactory.getInstance().getServiceType(DaoType.ORDER);
        return orderDao.getTodayTotalOrdersCount();
    }
    public Double getTotalOrderSum(){
        OrderDao orderDao = DaoFactory.getInstance().getServiceType(DaoType.ORDER);
        return orderDao.getTotalOrderSum();
    }
    public String getTopCashierToday(){
        OrderDao orderDao = DaoFactory.getInstance().getServiceType(DaoType.ORDER);
        return orderDao.getTopCashierToday();
    }
    public String getTopItemToday(){
        OrderDao orderDao = DaoFactory.getInstance().getServiceType(DaoType.ORDER);
        return orderDao.getTopItemToday();
    }
}
