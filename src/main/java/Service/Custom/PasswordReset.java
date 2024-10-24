package Service.Custom;

import Service.SuperService;

public interface PasswordReset  extends SuperService {
    public boolean updatePassword(String email, String newPassword);
}
