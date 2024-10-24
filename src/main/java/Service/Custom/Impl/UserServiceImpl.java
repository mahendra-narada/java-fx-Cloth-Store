package Service.Custom.Impl;

import Entity.UserEntity;
import Repository.Custom.UserDao;
import Repository.DaoFactory;
import Service.Custom.UserService;
import model.UserModel;
import org.modelmapper.ModelMapper;
import util.DaoType;

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
}
