package Service.Custom.Impl;

import Entity.UserEntity;
import Repository.Custom.LoginDao;
import Repository.DaoFactory;
import Service.Custom.LoginService;
import util.DaoType;

public class LoginServiceImpl implements LoginService {

    @Override
    public UserEntity verifyUSer(String email, String password) {
        LoginDao loginDao = DaoFactory.getInstance().getServiceType(DaoType.LOGINUSER);
        UserEntity user = loginDao.verifyUser(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;

    }
    @Override
    public boolean verifyOnlyEmail(String email) {
        LoginDao loginDao = DaoFactory.getInstance().getServiceType(DaoType.LOGINUSER);
        UserEntity user = loginDao.verifyUser(email);
        return user != null;
    }
    }


