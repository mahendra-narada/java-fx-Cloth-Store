package Service.Custom.Impl;

import Entity.ItemEntity;
import Repository.Custom.ItemDao;
import Repository.DaoFactory;
import Service.Custom.ItemService;
import model.Item;
import org.modelmapper.ModelMapper;
import util.DaoType;

import java.util.List;

public class ItemServiceImpl implements ItemService {

    @Override
    public boolean addItem(Item item) {
        System.out.println("Service Layer"+item);
        ItemEntity itemEntity = new ModelMapper().map(item,ItemEntity.class);
        ItemDao itemDao= DaoFactory.getInstance().getServiceType(DaoType.ITEM);
        itemDao.addItem(itemEntity);
        return true;
    }

    @Override
    public int fetchLastItemId() {
        ItemDao itemDao = DaoFactory.getInstance().getServiceType(DaoType.ITEM);
        int userid= itemDao.getLastItemId();
        userid=userid+1;
        return userid;
    }

    @Override
    public List<ItemEntity> getAllItems() {
        ItemDao itemDao = DaoFactory.getInstance().getServiceType(DaoType.ITEM);
        return  itemDao.getAllItems();
    }

    @Override
    public boolean deleteItem(String itemcode) {
        ItemDao itemDao = DaoFactory.getInstance().getServiceType(DaoType.ITEM);
        itemDao.deleteItem(itemcode);
        return true;
    }

    @Override
    public boolean updateItem(Item item) {
        ItemEntity itemEntity = new ModelMapper().map(item,ItemEntity.class);
        ItemDao itemDao= DaoFactory.getInstance().getServiceType(DaoType.ITEM);
        itemDao.updateItem(itemEntity);
        return true;
    }

    @Override
    public List<ItemEntity> searchItems(String searchText) {
        //ItemEntity itemEntity = new ModelMapper().map(item,ItemEntity.class);
        ItemDao itemDao= DaoFactory.getInstance().getServiceType(DaoType.ITEM);
        List<ItemEntity> items =itemDao.searchItems(searchText);
        return  items;


    }

    @Override
    public int getItemStock(String itemName) {
        ItemDao itemDao= DaoFactory.getInstance().getServiceType(DaoType.ITEM);
        return itemDao.getItemStock(itemName);
    }

    @Override
    public long getTotalItemsCount() {
        ItemDao itemDao= DaoFactory.getInstance().getServiceType(DaoType.ITEM);
        return itemDao.getTotalItemsCount();
    }

    public void updateItemStock(String itemName, int newStock){
        ItemDao itemDao= DaoFactory.getInstance().getServiceType(DaoType.ITEM);
        itemDao.updateItemStock(itemName,newStock);
    }
}
