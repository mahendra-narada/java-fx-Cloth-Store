package controller;

import Service.Custom.LoginService;
import Service.Custom.PasswordReset;
import Service.ServiceFactory;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import util.ServiceType;

import java.io.IOException;
import java.util.Objects;

public class PasswordResetPageController {

    @FXML
    private JFXButton btnLogin;


    @FXML
    private JFXTextField txtConfirmNewpassword;

    @FXML
    private JFXTextField txtNewpassword;

    private String userEmail;

    public void settingUserEmail(String email) {
        this.userEmail = email;
    }

    @FXML
    void btnChangePasswordOnAction(ActionEvent event) throws IOException {
        if (!Objects.equals(txtNewpassword.getText(), "") && !Objects.equals(txtConfirmNewpassword.getText(), "")) {
            if (Objects.equals(txtConfirmNewpassword.getText(), txtNewpassword.getText())) {
                PasswordReset passwordReset = ServiceFactory.getInstance().getServiceType(ServiceType.RESETPASSWORD);
                boolean isUpdated = passwordReset.updatePassword(userEmail, txtNewpassword.getText());

                if (isUpdated) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Password Reset Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Password has been reset successfully!");
                    alert.showAndWait();

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/LoginPage.fxml"));
                    Parent root = loader.load();
                    Stage currentStage = (Stage) btnLogin.getScene().getWindow();
                    currentStage.setScene(new Scene(root));
                    currentStage.show();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Password Reset Failed");
                    alert.setHeaderText(null);
                    alert.setContentText("Please try Again");
                    alert.showAndWait();
                }
            } else {

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Password Mismatch");
                alert.setHeaderText(null);
                alert.setContentText("Passwords do not match. Please try again.");
                alert.showAndWait();
            }
        } else {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Input Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in both password fields.");
            alert.showAndWait();
        }
    }

}
