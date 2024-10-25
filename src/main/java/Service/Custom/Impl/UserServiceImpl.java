package Service.Custom.Impl;

import Entity.UserEntity;
import Entity.UserType;
import Repository.Custom.UserDao;
import Repository.DaoFactory;
import Service.Custom.UserService;
import model.UserModel;
import org.modelmapper.ModelMapper;
import util.DaoType;

import java.util.List;

public class UserServiceImpl implements UserService {
    @Override
    public boolean addCustomer(UserModel userModel) {
        System.out.println("Service Layer"+userModel);
        UserEntity userEntity = new ModelMapper().map(userModel,UserEntity.class);
        UserDao registerUserDao= DaoFactory.getInstance().getServiceType(DaoType.FIRSTREGISTERUSER);
        registerUserDao.addUser(userEntity);
        return true;
    }

    @Override
    public int fetchLastUserId() {
        UserDao registerUserDao= DaoFactory.getInstance().getServiceType(DaoType.FIRSTREGISTERUSER);
        int userid= registerUserDao.getLastUserId();
        userid=userid+1;
        return userid;
    }

    @Override
    public UserModel getUser(String email) {
        UserDao registerUserDao= DaoFactory.getInstance().getServiceType(DaoType.FIRSTREGISTERUSER);
        UserEntity userEntity = registerUserDao.getUser(email);
        UserModel userModel = new ModelMapper().map(userEntity,UserModel.class);
        return userModel;
    }

    @Override
    public void updateUser(UserModel userModel) {
        UserDao userDao = DaoFactory.getInstance().getServiceType(DaoType.FIRSTREGISTERUSER);
        UserEntity userEntity = new ModelMapper().map(userModel,UserEntity.class);
        userDao.updateUser(userEntity);

    }

    @Override
    public List<UserEntity> getAllUsers() {
        UserDao userDao = DaoFactory.getInstance().getServiceType(DaoType.FIRSTREGISTERUSER);
        return userDao.getAllUsers();
    }

    @Override
    public void deleteUser(int userId) {
        UserDao userDao = DaoFactory.getInstance().getServiceType(DaoType.FIRSTREGISTERUSER);
        userDao.deleteUser(userId);
    }

    @Override
    public void updateUser(int userId, String newUserName, String newEmail, String newPassword, UserType newUserType, byte[] newImageData) {
        UserDao userDao = DaoFactory.getInstance().getServiceType(DaoType.FIRSTREGISTERUSER);
        userDao.updateUser(userId,newUserName,newEmail,newPassword,newUserType,newImageData);
    }
}
