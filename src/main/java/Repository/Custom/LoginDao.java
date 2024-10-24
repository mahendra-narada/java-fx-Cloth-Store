package Repository.Custom;

import Entity.UserEntity;
import Repository.SuperDao;

public interface LoginDao extends SuperDao {
    public UserEntity verifyUser(String email);
}
