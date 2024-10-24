package Service.Custom.Impl;

import Repository.Custom.LoginDao;
import Repository.Custom.ResetPasswordDao;
import Repository.DaoFactory;
import Service.Custom.PasswordReset;
import util.DaoType;

public class PsswordResetImpl implements PasswordReset {

    @Override
    public boolean updatePassword(String email, String newPassword) {
        ResetPasswordDao resetPasswordDao = DaoFactory.getInstance().getServiceType(DaoType.RESETPASSWORD);
        return resetPasswordDao.updatePassword(email, newPassword);
    }
}
