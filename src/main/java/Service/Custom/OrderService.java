package Service.Custom;


import Entity.OrderEntity;
import Service.SuperService;
import model.CartItem;

import java.util.List;

public interface OrderService extends SuperService {
    public int fetchLastItemId();
    boolean placeOrder(String orderID,String customerName, String customerEmail, List<CartItem> cartItems,String cashier);
    public List<OrderEntity> getAllOrders();
}
