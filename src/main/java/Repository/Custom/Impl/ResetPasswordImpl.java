package Repository.Custom.Impl;

import Repository.Custom.ResetPasswordDao;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

public class ResetPasswordImpl implements ResetPasswordDao {

    @Override
    public boolean updatePassword(String email, String newPassword) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();

            Query query = session.createQuery("UPDATE UserEntity u SET u.password = :newPassword WHERE u.email = :email");
            query.setParameter("newPassword", newPassword);
            query.setParameter("email", email);

            int result = query.executeUpdate(); // Execute the update query
            transaction.commit();

            return result > 0; // return true if the password was updated successfully

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Rollback transaction if any error occurs
            }
            e.printStackTrace();
            return false; // return false if an error occurred
        }
    }
}
