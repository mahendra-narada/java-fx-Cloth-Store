package Repository.Custom.Impl;

import Entity.ItemEntity;
import Repository.Custom.ItemDao;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.util.List;

public class ItemDaoImpl implements ItemDao {


    @Override
    public boolean addItem(ItemEntity itemEntity) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        session.persist(itemEntity);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    @Override
    public int getLastItemId() {
        int lastUserId = -1;

        try (Session session = HibernateUtil.getSession()) {
            // Use HQL (Hibernate Query Language) to get the max userId from the UserEntity
            Query<Integer> query = session.createQuery("SELECT MAX(u.item_code) FROM ItemEntity u", Integer.class);
            lastUserId = query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lastUserId;
    }

    @Override
    public List<ItemEntity> getAllItems() {
        Session session = HibernateUtil.getSession();
        List<ItemEntity> itemList = null;
        try {
            session.beginTransaction();
            itemList = session.createQuery("FROM ItemEntity", ItemEntity.class).getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return itemList;
    }

    @Override
    public boolean deleteItem(String itemcode) {
        try {
            Session session = HibernateUtil.getSession();
            session.beginTransaction();
            session.remove(session.get(ItemEntity.class,itemcode));
            session.getTransaction().commit();
            return true;
        }catch (HibernateException e){
            return false;
        }
    }

    @Override
    public boolean updateItem(ItemEntity itemEntity) {
        try {
            Session session = HibernateUtil.getSession();
            session.beginTransaction();
            session.merge(itemEntity);
            session.getTransaction().commit();
            session.close();
            return true;
        }catch (HibernateException e){
            return false;
        }
    }

    @Override
    public List<ItemEntity> searchItems(String searchText) {
        Session session = HibernateUtil.getSession();
        try {
            // HQL query to search by item name, supplier name, or category
            String hql = "FROM ItemEntity i WHERE i.item_name LIKE :searchText " +
                    "OR i.supplierName LIKE :searchText " +
                    "OR i.category LIKE :searchText"; // Added search by category

            Query<ItemEntity> query = session.createQuery(hql, ItemEntity.class);
            query.setParameter("searchText", "%" + searchText + "%");

            return query.list();
        } finally {
            session.close();
        }
    }

}
