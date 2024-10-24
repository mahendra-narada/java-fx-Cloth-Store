package Service;

import Service.Custom.Impl.*;
import util.ServiceType;

public class ServiceFactory {

    private static ServiceFactory instance;

    private ServiceFactory() {
    }


    public static ServiceFactory getInstance() {

        return instance == null ? instance = new ServiceFactory() : instance;
    }

    public <T extends SuperService> T getServiceType(ServiceType type) {
        switch (type) {
            case LOGINUSER:
                return (T) new LoginServiceImpl();
            case RESETPASSWORD:
                return (T) new PsswordResetImpl();
            case FIRSTREGISTERUSER:
                return (T) new UserServiceImpl();
            case ITEM:
                return (T) new ItemServiceImpl();
            case ORDER:
                return (T) new OrderServiceImpl();

        }
        return null;
    }
}
