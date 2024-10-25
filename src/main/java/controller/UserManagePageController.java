package controller;

import Entity.ItemEntity;
import Entity.UserEntity;
import Entity.UserType;
import Service.Custom.Impl.UserServiceImpl;
import Service.Custom.UserService;
import Service.ServiceFactory;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.UserModel;
import util.ServiceType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UserManagePageController  implements Initializable {

    @FXML
    private JFXCheckBox checkAdmin;

    @FXML
    private JFXCheckBox checkDefault;

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableColumn<?, ?> colPassword;

    @FXML
    private TableColumn<?, ?> colUserName;

    @FXML
    private TableColumn<?, ?> colUserType;

    @FXML
    private TableColumn<?, ?> colsalary;

    @FXML
    private JFXTextField txtPassowrd;

    @FXML
    private JFXTextField txtUserEmail;

    @FXML
    private Label nextuserid;


    @FXML
    private JFXTextField txtUsername;

    @FXML
    private ImageView userImage;

    @FXML
    private Label userid;
    @FXML
    private JFXTextField txtSalary;

    @FXML
    private TableView<UserEntity> userTable;

    private ObservableList<UserEntity> userEntityObservableList = FXCollections.observableArrayList();

    private String userEmail;
    public void settingUserEmail(String email) {
        this.userEmail = email;

    }
    @FXML
    void btnAdd(ActionEvent event) {
        String username = txtUsername.getText();
        String email = txtUserEmail.getText();
        String password = txtPassowrd.getText();
        boolean isNormalUserChecked = checkDefault.isSelected();
        boolean isAdminChecked = checkAdmin.isSelected();
        byte[] image = UserimageBytes;

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
        userModel.setUserId(Integer.parseInt(nextuserid.getText()));
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
                loadTable();
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

    @FXML
    void btnHome(ActionEvent event) throws IOException {

        UserService userService = ServiceFactory.getInstance().getServiceType(ServiceType.FIRSTREGISTERUSER);
        UserModel userModel = userService.getUser(userEmail);
        if (userModel.getUserType() == UserType.ADMIN_USER) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/AdminDashBoard.fxml"));
            Parent root = loader.load();
            AdminDashBoardPageController adminDashBoardPageController = loader.getController();
            adminDashBoardPageController.settingUserEmail(userEmail);
            Stage currentStage = (Stage) txtUserEmail.getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.show();
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/NormalUserDashBoardPage.fxml"));
            Parent root = loader.load();
            NormalUserDashBoardPageController normalUserDashBoardPageController = loader.getController();
            normalUserDashBoardPageController.settingUserEmail(userEmail);
            Stage currentStage = (Stage) txtUserEmail.getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.show();

        }
    }

    @FXML
    void btnRemove(ActionEvent event) {
        UserServiceImpl userService = ServiceFactory.getInstance().getServiceType(ServiceType.FIRSTREGISTERUSER);
        UserModel userModel = userService.getUser(txtUserEmail.getText());

        // Check if the user was found
        if (userModel != null) {
            int userId = userModel.getUserId();
            userService.deleteUser(userId);

            // Show success or failure alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("User removed successfully.");
            alert.showAndWait();
            loadTable();
        } else {
            // Show alert if user is not found
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("No user found with the provided email.");
            alert.showAndWait();
        }
    }


    @FXML
    void btnUpdate(ActionEvent event) {
        // Retrieve and validate input values
        String username = txtUsername.getText().trim();
        String email = txtUserEmail.getText().trim();
        String password = txtPassowrd.getText().trim();

        double salary;
        int id;

        // Validate if all fields are filled
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()  || userid.getText().isEmpty()) {
            showAlert("Error", "All fields must be filled.", Alert.AlertType.ERROR);
            return;
        }


        // Validate if id is a valid integer
        try {
            id = Integer.parseInt(userid.getText().trim());
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid user ID.", Alert.AlertType.ERROR);
            return;
        }

        // Validate if email is in a correct format
        if (!isValidEmail(email)) {
            showAlert("Error", "Please enter a valid email address.", Alert.AlertType.ERROR);
            return;
        }

        // Set user type based on checkbox selection
        UserType userType = null;
        if (checkAdmin.isSelected()) {
            userType = UserType.ADMIN_USER;
        } else if (checkDefault.isSelected()) {
            userType = UserType.DEFAULT_USER;
        } else {
            showAlert("Error", "Please select a user type.", Alert.AlertType.ERROR);
            return;
        }

        // Update user details
        UserServiceImpl userService = ServiceFactory.getInstance().getServiceType(ServiceType.FIRSTREGISTERUSER);
        userService.updateUser(id, username, email, password,userType, UserimageBytes);

        // Show success message
        showAlert("Success", "User details updated successfully!", Alert.AlertType.INFORMATION);
        loadTable();
    }

    // Method to check email validity
    private boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@[\\w-\\.]+\\.[a-zA-Z]{2,4}$";
        return email.matches(emailRegex);
    }

    // Method to display alerts
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private byte[] UserimageBytes;

    @FXML
    void btnUpload(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File file = fileChooser.showOpenDialog(userImage.getScene().getWindow());
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            userImage.setImage(image);
            UserimageBytes = convertImageToBytes(file);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colUserType.setCellValueFactory(new PropertyValueFactory<>("userType"));
        colsalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        loadTable();
        userTable.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldValue, newValue) -> {
            if(newValue!=null){
                setTextToValues(newValue);
                displayItemImage(newValue.getImageData());

            }

        }));
        UserServiceImpl registerUser = ServiceFactory.getInstance().getServiceType(ServiceType.FIRSTREGISTERUSER);
        int userid=registerUser.fetchLastUserId();
        nextuserid.setText(String.valueOf(userid));

    }
    private void displayItemImage(byte[] imageData) {
        if (imageData != null && imageData.length > 0) {
            // Convert byte[] to Image
            Image image = new Image(new ByteArrayInputStream(imageData));
            //Image supimage = new Image(new ByteArrayInputStream(UserimageBytes));
            userImage.setImage(image);


        } else {

            userImage.setImage(null);
        }
    }

    private void setTextToValues(UserEntity newValue) {
        txtUsername.setText(newValue.getUserName());
        txtPassowrd.setText(newValue.getPassword());
        txtUserEmail.setText(newValue.getEmail());
        System.out.println("User Type: " + newValue.getUserType());
        if (newValue.getUserType() == UserType.DEFAULT_USER) {
            checkDefault.setSelected(true);  // Check the 'default' checkbox
            checkAdmin.setSelected(false);    // Uncheck the 'admin' checkbox
        } else if (newValue.getUserType() == UserType.ADMIN_USER) {
            checkDefault.setSelected(false);   // Uncheck the 'default' checkbox
            checkAdmin.setSelected(true);      // Check the 'admin' checkbox
        } else {
            // Uncheck both if user type is neither
            checkDefault.setSelected(false);
            checkAdmin.setSelected(false);
        }
        userid.setText(String.valueOf(newValue.getUserId()));
    }

    public void loadTable() {
        UserServiceImpl userService = ServiceFactory.getInstance().getServiceType(ServiceType.FIRSTREGISTERUSER);
        List<UserEntity> allUsers  =userService.getAllUsers();
        userEntityObservableList.clear();
        userEntityObservableList.addAll(allUsers);
        userTable.setItems(userEntityObservableList);

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
}
