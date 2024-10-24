package controller;

import Entity.UserType;
import Service.Custom.Impl.UserServiceImpl;
import Service.ServiceFactory;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import model.UserModel;
import util.ServiceType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserRegisterFormPageController implements Initializable {

    @FXML
    private JFXTextField txtUsername;

    @FXML
    private JFXTextField txtpassword;

    @FXML
    private JFXTextField txtuserEmail;

    @FXML
    private ImageView userImage;

    @FXML
    private JFXCheckBox txtNormalUsercheck;

    @FXML
    private JFXCheckBox txtadmincheck;

    @FXML
    private Label lbluserId;

    private byte[] imageBytes;

    @FXML
    void btnAddImage(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File file = fileChooser.showOpenDialog(userImage.getScene().getWindow());
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            userImage.setImage(image);
            imageBytes = convertImageToBytes(file);
        }
    }

    private byte[] convertImageToBytes(File file) {
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    @FXML
    void btnRegisterUser(ActionEvent event) {
        String username = txtUsername.getText();
        String email = txtuserEmail.getText();
        String password = txtpassword.getText();
        boolean isNormalUserChecked = txtNormalUsercheck.isSelected();
        boolean isAdminChecked = txtadmincheck.isSelected();
        byte[] image = imageBytes;

        boolean verifyEmail = isValidEmail(email);
        // Validate fields
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || image == null || image.length == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Registration Failed");
            alert.setHeaderText("Missing Fields");
            alert.setContentText("Please fill in all the required fields and upload an image.");
            alert.showAndWait();
            return;
        }

        // Create and populate the UserModel
        UserModel userModel = new UserModel();
        userModel.setUserId(Integer.parseInt(lbluserId.getText()));
        userModel.setUserName(username);
        userModel.setEmail(email);
        userModel.setPassword(password);
        userModel.setImageData(image);

        // Check user type selection
        if (isNormalUserChecked) {
            userModel.setUserType(UserType.DEFAULT_USER);
        } else if (isAdminChecked) {
            userModel.setUserType(UserType.ADMIN_USER);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Registration Failed");
            alert.setHeaderText("User Type Not Selected");
            alert.setContentText("Please select either Normal User or Admin User.");
            alert.showAndWait();
            return;
        }
        if (verifyEmail) {

            // Call the registration service
            UserServiceImpl registerUser = ServiceFactory.getInstance().getServiceType(ServiceType.FIRSTREGISTERUSER);
            boolean registrationSuccessful = registerUser.addCustomer(userModel);

            // Success or failure alerts
            if (registrationSuccessful) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registration Successful");
                alert.setHeaderText("User Registered");
                alert.setContentText("User has been successfully registered!");
                alert.showAndWait();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Registration Failed");
            alert.setHeaderText("Registration Error");
            alert.setContentText("Enter a Valid Email");
            alert.showAndWait();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UserServiceImpl registerUser = ServiceFactory.getInstance().getServiceType(ServiceType.FIRSTREGISTERUSER);
        int userid=registerUser.fetchLastUserId();
        lbluserId.setText(String.valueOf(userid));
    }


    private boolean isValidEmail(String email) {
        // Simple regex for basic email validation
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }
}
