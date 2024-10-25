package controller;

import Entity.UserType;
import Service.Custom.Impl.UserServiceImpl;
import Service.Custom.LoginService;
import Service.Custom.UserService;
import Service.ServiceFactory;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.UserModel;
import util.ServiceType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class ViewUserProfilePageController implements Initializable {

    @FXML
    private JFXTextField lblEmail;

    @FXML
    private JFXTextField lblpassword;

    @FXML
    private JFXTextField lbluserName;

    @FXML
    private ImageView userImage;

    private String userEmail;

    public void settingUserEmail(String email) {
        this.userEmail = email;
        SettingUpUserInfo();

    }

    public void SettingUpUserInfo(){
        if (userEmail == null || userEmail.isEmpty()) {
            System.out.println("Email is not set yet.");
            return;
        }

        UserService userService = ServiceFactory.getInstance().getServiceType(ServiceType.FIRSTREGISTERUSER);
        UserModel userModel = userService.getUser(userEmail);
        System.out.println(userEmail);
        lbluserName.setText(userModel.getUserName());
        lblEmail.setText(userModel.getEmail());
        lblpassword.setText(userModel.getPassword());

        byte[] imageBytes = userModel.getImageData();
        if (imageBytes != null && imageBytes.length > 0) {
            InputStream inputStream = new ByteArrayInputStream(imageBytes);
            Image image = new Image(inputStream);
            userImage.setImage(image); // Assuming 'userImage' is your ImageView
        } else {
            System.out.println("No image available for the user.");
        }

    }

    @FXML
    void btnUpdateUser(ActionEvent event) {
        String newUsername = lbluserName.getText();
        String newpassword = lblpassword.getText();
        String newemail = lblEmail.getText();
        byte[] newimageBytes = imageBytes;
        UserServiceImpl userService = ServiceFactory.getInstance().getServiceType(ServiceType.FIRSTREGISTERUSER);
         UserModel userModel=userService.getUser(userEmail);
         UserModel userModel1 = new UserModel(userModel.getUserId(),newUsername,newimageBytes,newemail,newpassword,userModel.getUserType());
         userService.updateUser(userModel1);

    }
    private byte[] imageBytes;

    @FXML
    void btnUploadImage(ActionEvent event) {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }


    @FXML
    void btnHomePge(ActionEvent event) throws IOException {
        UserService userService = ServiceFactory.getInstance().getServiceType(ServiceType.FIRSTREGISTERUSER);
        UserModel userModel = userService.getUser(userEmail);
        if (userModel.getUserType() == UserType.ADMIN_USER) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/AdminDashBoard.fxml"));
            Parent root = loader.load();
            AdminDashBoardPageController adminDashBoardPageController = loader.getController();
            adminDashBoardPageController.settingUserEmail(userEmail);
            Stage currentStage = (Stage) lblEmail.getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.show();
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/NormalUserDashBoardPage.fxml"));
            Parent root = loader.load();
            NormalUserDashBoardPageController normalUserDashBoardPageController = loader.getController();
            normalUserDashBoardPageController.settingUserEmail(userEmail);
            Stage currentStage = (Stage) lblEmail.getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.show();

        }
    }
}
