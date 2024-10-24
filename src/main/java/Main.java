import Entity.UserEntity;
import org.hibernate.Session;
import util.HibernateUtil;

public class Main {
    public static void main(String[] args) {
        Starter.main(args);
//
//        try (Session session = HibernateUtil.getSession()) {
//            session.beginTransaction();
//            UserEntity user = session.get(UserEntity.class, 1); // Replace 1 with an actual user ID
//            System.out.println(user);
//            session.getTransaction().commit();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    }
}