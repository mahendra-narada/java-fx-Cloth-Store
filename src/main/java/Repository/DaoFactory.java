package Repository;

import Repository.Custom.Impl.*;
import util.DaoType;

public class DaoFactory {

    public static DaoFactory instance;

    private DaoFactory() {}

    public static DaoFactory getInstance() {
        return instance==null ? instance=new DaoFactory() :instance;
    }

    public <T extends SuperDao>T getServiceType(DaoType type){
        switch (type){
            case LOGINUSER:return (T) new LoginDaoImpl();
            case RESETPASSWORD:return (T) new ResetPasswordImpl();
            case FIRSTREGISTERUSER:return (T) new UserDaoImpl();
            case ITEM:return (T) new ItemDaoImpl();
            case ORDER:return (T) new OrderDaoImpl();

        }
        return null;
    }
}
