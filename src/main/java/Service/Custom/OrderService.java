package Service.Custom;


import Entity.OrderDetailEntity;
import Entity.OrderEntity;
import Service.SuperService;
import model.CartItem;

import java.util.List;
import java.util.Map;

public interface OrderService extends SuperService {
    public int fetchLastItemId();
    boolean placeOrder(String orderID,String customerName, String customerEmail, List<CartItem> cartItems,String cashier);
    public List<OrderEntity> getAllOrders();
    public List<OrderDetailEntity> getAllItems();
    public List<OrderDetailEntity> getOrderDetailsByOrderId(String orderId);
    public Double getTodayTotalForUser(String cashier);
    public int getTodayTotalOrdersForUser(String cashier);
    public Map<String, Double> getDailySales();
    public boolean updateOrder(OrderEntity order);
    public Map<String, Long> getWeeklyOrderCount();
    public long getTodayTotalOrdersCount();
    public Double getTotalOrderSum();
    public String getTopCashierToday();
    public String getTopItemToday();

}
