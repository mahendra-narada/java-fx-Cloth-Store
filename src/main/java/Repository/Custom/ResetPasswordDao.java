package Repository.Custom;

import Repository.SuperDao;

public interface ResetPasswordDao extends SuperDao {
    public boolean updatePassword(String email, String newPassword);
}
