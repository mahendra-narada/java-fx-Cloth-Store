package Service.Custom;

import Entity.UserEntity;
import Service.SuperService;
import model.UserModel;

public interface UserService extends SuperService {
    boolean addCustomer(UserModel userModel);
    public int fetchLastUserId();
    UserModel getUser(String email);

}
