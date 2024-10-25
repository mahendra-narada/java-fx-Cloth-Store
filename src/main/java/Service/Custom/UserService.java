package Service.Custom;

import Entity.UserEntity;
import Entity.UserType;
import Service.SuperService;
import model.UserModel;

import java.util.List;

public interface UserService extends SuperService {
    boolean addCustomer(UserModel userModel);
    public int fetchLastUserId();
    UserModel getUser(String email);
    public void updateUser(UserModel userModel);
    public List<UserEntity> getAllUsers();
    public void deleteUser(int userId);
    public void updateUser(int userId, String newUserName, String newEmail, String newPassword, Double newSalary, UserType newUserType, byte[] newImageData);
}
