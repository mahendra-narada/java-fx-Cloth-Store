package Service.Custom.Impl;

import Entity.OrderEntity;
import Repository.Custom.ItemDao;
import Repository.Custom.OrderDao;
import Repository.DaoFactory;
import Service.Custom.OrderService;
import model.CartItem;
import util.DaoType;

import java.util.List;

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
}
