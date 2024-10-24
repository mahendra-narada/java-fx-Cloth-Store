package Repository.Custom.Impl;

import Entity.UserEntity;
import Repository.Custom.LoginDao;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import org.hibernate.query.Query;

public class LoginDaoImpl implements LoginDao {

    @Override
    public UserEntity verifyUser(String email) {
        UserEntity user = null;

        try (Session session = HibernateUtil.getSession()) {
            Transaction transaction = session.beginTransaction(); // Initialize transaction

            // Correct the HQL query to use the entity name
            user = session.createQuery("FROM UserEntity WHERE email = :email", UserEntity.class)
                    .setParameter("email", email)
                    .uniqueResult();

            transaction.commit(); // Commit the transaction
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
        }

        return user; // Return the user entity (or null if not found)
    }

}

