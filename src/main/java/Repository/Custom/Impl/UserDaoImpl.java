package Repository.Custom.Impl;

import Entity.UserEntity;
import Entity.UserType;
import Repository.Custom.UserDao;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void updateUser(UserEntity userEntity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            // Start transaction
            transaction = session.beginTransaction();

            // Update the user in the database
            session.update(userEntity);

            // Commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public List<UserEntity> getAllUsers() {
        List<UserEntity> users = new ArrayList<>();

        try (Session session = HibernateUtil.getSession()) {
            // Use HQL to get all users
            Query<UserEntity> query = session.createQuery("FROM UserEntity", UserEntity.class);
            users = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    public void deleteUser(int userId) {
        try (Session session = HibernateUtil.getSession()) {
            Transaction transaction = session.beginTransaction();

            UserEntity user = session.get(UserEntity.class, userId);
            if (user != null) {
                session.delete(user); // Remove the user from the database
                transaction.commit();  // Commit the transaction
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUser(int userId, String newUserName, String newEmail, String newPassword,UserType newUserType, byte[] newImageData) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSession()) {
            // Begin a transaction
            transaction = session.beginTransaction();

            // Retrieve the user by ID
            UserEntity userEntity = session.get(UserEntity.class, userId);

            // Check if user exists
            if (userEntity != null) {
                // Update user details
                userEntity.setUserName(newUserName);
                userEntity.setEmail(newEmail);
                userEntity.setPassword(newPassword);
                //userEntity.setSalary(newSalary);
                userEntity.setUserType(newUserType);

                // Update image data
                if (newImageData != null) {
                    userEntity.setImageData(newImageData);
                }

                // Persist the changes
                session.update(userEntity);
            } else {
                System.out.println("User with ID " + userId + " not found.");
            }

            // Commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();  // Rollback in case of error
            }
            e.printStackTrace();
        }
    }


}
