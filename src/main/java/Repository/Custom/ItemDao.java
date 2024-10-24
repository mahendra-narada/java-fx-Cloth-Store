package Repository.Custom;

import Entity.ItemEntity;
import Repository.CrudDao;
import Repository.SuperDao;
import Service.Custom.ItemService;

import java.util.List;

public interface ItemDao extends SuperDao {
    boolean addItem(ItemEntity itemEntity);
    public int getLastItemId();
    public List<ItemEntity> getAllItems();
    public  boolean deleteItem(String itemcode);
    public boolean updateItem(ItemEntity itemEntity);
    public List<ItemEntity> searchItems(String searchText);

}
