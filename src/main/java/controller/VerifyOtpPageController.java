package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class VerifyOtpPageController {

    @FXML
    private JFXButton btnVerifyOTP;

    @FXML
    private Hyperlink linkOTPSendAgain;

    @FXML
    private JFXTextField txtOTP;

    private String userEmail;

    public void settingUserEmail(String email) {
        this.userEmail = email;
    }

    @FXML
    void btnVerifyOTPOnAction(ActionEvent event) throws IOException {
        if (Objects.equals(txtOTP.getText(), OtpStorage.getStoredOtp())) {
            System.out.println(userEmail);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/PasswordResetPage.fxml"));
            Parent root = loader.load();
            PasswordResetPageController passwordResetPageController = loader.getController();
            passwordResetPageController.settingUserEmail(userEmail);
            Stage currentStage = (Stage) btnVerifyOTP.getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.show();

        } else {

        }
    }






}
