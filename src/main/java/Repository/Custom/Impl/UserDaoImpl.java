package Repository.Custom.Impl;

import Entity.UserEntity;
import Repository.Custom.UserDao;
import org.hibernate.Session;
import org.hibernate.query.Query;
import util.HibernateUtil;

public class UserDaoImpl implements UserDao {
    @Override
    public boolean addUser(UserEntity userEntity) {
        System.out.println("Repo "+userEntity);

        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        session.persist(userEntity);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    @Override
    public UserEntity getUser(String email) {
        // Get the current Hibernate session
        Session session = HibernateUtil.getSession();
        UserEntity userEntity = null;

        try {
            // Begin transaction
            session.beginTransaction();

            // Create HQL query to fetch the user by email
            String hql = "FROM UserEntity u WHERE u.email = :email";
            Query<UserEntity> query = session.createQuery(hql, UserEntity.class);
            query.setParameter("email", email);

            // Fetch the single result (or handle no result case)
            userEntity = query.uniqueResult();

            // Commit the transaction
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback(); // Rollback in case of an error
            }
            e.printStackTrace();
        } finally {
            session.close(); // Close the session
        }

        return userEntity;
    }


    @Override
    public int getLastUserId() {
        int lastUserId = -1;

        try (Session session = HibernateUtil.getSession()) {
            // Use HQL (Hibernate Query Language) to get the max userId from the UserEntity
            Query<Integer> query = session.createQuery("SELECT MAX(u.userId) FROM UserEntity u", Integer.class);
            lastUserId = query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lastUserId;
    }

}
