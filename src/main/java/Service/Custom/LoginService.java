package Service.Custom;

import Entity.UserEntity;
import Service.SuperService;

public interface LoginService extends SuperService {
    public UserEntity verifyUSer(String email,String password);
    public boolean verifyOnlyEmail(String email);
}

