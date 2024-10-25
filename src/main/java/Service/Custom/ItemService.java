package Service.Custom;

import Entity.ItemEntity;
import Service.SuperService;
import model.Item;

import java.util.List;

public interface ItemService extends SuperService {
    boolean addItem(Item item);
    public int fetchLastItemId();
    public List<ItemEntity> getAllItems();
    boolean deleteItem(String itemcode);
    boolean updateItem(Item item);
    public List<ItemEntity> searchItems(String searchText);
    public int getItemStock(String itemName);
    public long getTotalItemsCount();

}
