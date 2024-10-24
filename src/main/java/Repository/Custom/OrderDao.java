package Repository.Custom;

import Entity.OrderDetailEntity;
import Entity.OrderEntity;
import Repository.CrudDao;
import Repository.SuperDao;
import javafx.collections.ObservableList;
import model.CartItem;

import java.util.List;

public interface OrderDao extends SuperDao {
    public boolean saveOrder(String orderId,String customerName, String customerEmail, List<CartItem> cartItems,String cashier);
    int getLastOrderId();
    public List<OrderEntity> getAllOrders();

}
