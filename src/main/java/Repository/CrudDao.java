package Repository;

import Entity.UserEntity;

public interface CrudDao<T> extends SuperDao {
    boolean addUser(T t);
    UserEntity getUser(String email);

}
