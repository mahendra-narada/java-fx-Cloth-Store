package controller;

import Entity.UserEntity;
import Entity.UserType;
import Repository.Custom.LoginDao;
import Repository.DaoFactory;
import Service.Custom.LoginService;
import Service.ServiceFactory;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;
import util.DaoType;
import util.ServiceType;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class LoginPageController {

    @FXML
    private JFXButton btnLogin;

    @FXML
    private Hyperlink linkForgotPassword;

    @FXML
    private JFXTextField txtemail;

    @FXML
    private JFXTextField txtpassword;



    @FXML
    void btnLoginOnAction(ActionEvent event) throws IOException {
        LoginService loginService = ServiceFactory.getInstance().getServiceType(ServiceType.LOGINUSER);
        String email = txtemail.getText();
        String password = txtpassword.getText();

        if (!Objects.equals(email, "") && !Objects.equals(password, "")) {

            if (isValidEmail(email)) {

                UserEntity userEntity = loginService.verifyUSer(email, password);
                if (userEntity != null) {
                    if (userEntity.getUserType() == UserType.ADMIN_USER) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/AdminDashBoardPage.fxml"));
                        Parent root = loader.load();
                        AdminDashBoardPageController adminDashBoardPageController = loader.getController();
                        adminDashBoardPageController.settingUserEmail(email);
                        Stage currentStage = (Stage) btnLogin.getScene().getWindow();
                        currentStage.setScene(new Scene(root));
                        currentStage.show();
                    } else {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/NormalUserDashBoardPage.fxml"));
                        Parent root = loader.load();
                        NormalUserDashBoardPageController normalUserDashBoardPageController = loader.getController();
                        normalUserDashBoardPageController.settingUserEmail(email);
                        Stage currentStage = (Stage) btnLogin.getScene().getWindow();
                        currentStage.setScene(new Scene(root));
                        currentStage.show();

                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Login Failed");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid email or password. Please try again.");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText(null);
                alert.setContentText("Invalid email Please try again.");
                alert.showAndWait();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText(null);
            alert.setContentText("Please input All Fields ");
            alert.showAndWait();
        }
    }

    private boolean isValidEmail(String email) {
        // Simple regex for basic email validation
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }


    @FXML
    void btnForgotPassword(ActionEvent event) throws IOException {

       if (!Objects.equals(txtemail.getText(), "")) {
           if (isValidEmail(txtemail.getText())) {
               LoginService loginService = ServiceFactory.getInstance().getServiceType(ServiceType.LOGINUSER);
                if (loginService.verifyOnlyEmail(txtemail.getText())) {
                   String otp=OTPSender.generateOTP(6);
                    OTPSender.sendOTP(txtemail.getText(), otp);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/VerifyOtpPage.fxml")); // Ensure the path is correct
                    Parent root = loader.load();
                    VerifyOtpPageController verifyOtpPageController = loader.getController();
                    verifyOtpPageController.settingUserEmail(txtemail.getText());
                    Stage currentStage = (Stage) btnLogin.getScene().getWindow();
                    currentStage.setScene(new Scene(root));
                    currentStage.show();

                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Password Reset Failed");
                    alert.setHeaderText(null);
                    alert.setContentText("Please Enter an Valid Email");
                    alert.showAndWait();
                }
           }
           else {
               Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setTitle("Password Reset Failed");
               alert.setHeaderText(null);
               alert.setContentText("Please Enter an Valid Email");
               alert.showAndWait();
           }
       }
       else {
           Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setTitle("Password Reset Failed");
           alert.setHeaderText(null);
           alert.setContentText("Please Enter an Email");
           alert.showAndWait();
       }
    }



    @FXML
    void btnRegisterUserPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/UserRegisterForm.fxml"));
        Parent root = loader.load();
        Stage currentStage = (Stage) btnLogin.getScene().getWindow();
        currentStage.setScene(new Scene(root));
        currentStage.show();
    }

}
