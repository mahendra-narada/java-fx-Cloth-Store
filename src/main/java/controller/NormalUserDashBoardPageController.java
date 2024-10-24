package controller;

import Entity.UserEntity;
import Service.Custom.LoginService;
import Service.Custom.UserService;
import Service.ServiceFactory;
import com.jfoenix.controls.JFXButton;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.UserModel;
import org.modelmapper.ModelMapper;
import util.ServiceType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class NormalUserDashBoardPageController  implements Initializable {

    @FXML
    private BarChart<?, ?> Graph;

    @FXML
    private ImageView UserImageId;

    @FXML
    private Rectangle lblActiveHours;

    @FXML
    private Rectangle lblActiveHours1;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblSlogan;

    @FXML
    private Label lblTime;

    @FXML
    private Rectangle lblTotal;

    @FXML
    private Label lblTotalItems;

    @FXML
    private Rectangle lblTotalOrders;

    @FXML
    private JFXButton lblusername;

    private String userEmail;

    public void settingUserEmail(String email) {
        this.userEmail = email;
        SettingUpUserInfo();
    }

    @FXML
    void btnCall(ActionEvent event) {

    }

    @FXML
    void btnCheckSalary(ActionEvent event) {

    }

    @FXML
    void btnInventoryMangeOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/InventoryManagmentPage.fxml"));
        Parent root = loader.load();
        InventoryManagmentPageController inventoryManagmentPageController = loader.getController();
        inventoryManagmentPageController.settingUserEmail(userEmail);
        Stage currentStage = (Stage) lblTime.getScene().getWindow();
        currentStage.setScene(new Scene(root));
        currentStage.show();
    }

    @FXML
    void btnLogOutOnAction(ActionEvent event) {

    }

    @FXML
    void btnOrdersOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/OrderPage.fxml"));
        Parent root = loader.load();
        OrderPageController orderPageController = loader.getController();
        //orderPageController.settingUserEmail(userEmail);
        Stage currentStage = (Stage) lblTime.getScene().getWindow();
        currentStage.setScene(new Scene(root));
        currentStage.show();
    }

    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/PlaceOrderPage.fxml"));
        Parent root = loader.load();
        PlaceOrderPageController placeOrderPageController = loader.getController();
        placeOrderPageController.settingUserEmail(userEmail);
        Stage currentStage = (Stage) lblTime.getScene().getWindow();
        currentStage.setScene(new Scene(root));
        currentStage.show();

    }

    @FXML
    void btnSuppliersOnAction(ActionEvent event) {

    }

    @FXML
    void btnToDoList(ActionEvent event) {

    }

    @FXML
    void btnUSerUpdate(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displaycurrentDateAndTime();
        //SettingUpUserInfo();

    }

    public void SettingUpUserInfo(){
        if (userEmail == null || userEmail.isEmpty()) {
            System.out.println("Email is not set yet.");
            return;
        }

        UserService userService = ServiceFactory.getInstance().getServiceType(ServiceType.FIRSTREGISTERUSER);
        UserModel userModel = userService.getUser(userEmail);
        System.out.println(userEmail);
        //System.out.println(userModel.toString());
        lblusername.setText(userModel.getUserName());

        byte[] imageBytes = userModel.getImageData();
        if (imageBytes != null && imageBytes.length > 0) {
            InputStream inputStream = new ByteArrayInputStream(imageBytes);
            Image image = new Image(inputStream);
            UserImageId.setImage(image); // Assuming 'userImage' is your ImageView
        } else {
            System.out.println("No image available for the user.");
        }

    }

    public void displaycurrentDateAndTime() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(dateFormatter);
        lblDate.setText(formattedDate);

        // Create a Timeline to update the time every second
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String formattedTime = currentTime.format(timeFormatter);
            lblTime.setText(formattedTime);
        }),
                new KeyFrame(Duration.seconds(1)) // Update every second
        );
        clock.setCycleCount(Timeline.INDEFINITE); // Run the clock indefinitely
        clock.play();
    }


}
