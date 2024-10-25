package Repository.Custom;

import Entity.UserEntity;
import Repository.CrudDao;

public interface UserDao extends CrudDao<UserEntity> {

    public int getLastUserId();
    public void updateUser(UserEntity userEntity);
}
