package Repository.Custom;

import Entity.UserEntity;
import Entity.UserType;
import Repository.CrudDao;

import java.util.List;

public interface UserDao extends CrudDao<UserEntity> {

    public int getLastUserId();

    public void updateUser(UserEntity userEntity);

    public List<UserEntity> getAllUsers();

    public void deleteUser(int userId);

    public void updateUser(int userId, String newUserName, String newEmail, String newPassword, Double newSalary, UserType newUserType, byte[] newImageData);

}