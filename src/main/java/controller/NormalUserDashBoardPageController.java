package controller;

import Entity.UserEntity;
import Service.Custom.Impl.ItemServiceImpl;
import Service.Custom.Impl.OrderServiceImpl;
import Service.Custom.Impl.UserServiceImpl;
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
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
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
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.ResourceBundle;

public class NormalUserDashBoardPageController  implements Initializable {

    @FXML
    private BarChart<String, Number> Graph;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private ImageView UserImageId;


    @FXML
    private Rectangle lblActiveHours1;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblSlogan;

    @FXML
    private Label lblTime;

    @FXML
    private Label lblTotal;

    @FXML
    private Label lblTotalItems;

    @FXML
    private JFXButton lblusername;

    @FXML
    private Label lblActiveHours;

    @FXML
    private Label lblTotalOrders;

    private LocalTime loginTime;
    private Timeline timeline;


    private String userEmail;

    public void settingUserEmail(String email) {
        this.userEmail = email;
        SettingUpUserInfo();
        updateLabelWithTodayTotal();
        updateLabelWithTodayTotalOrders();
    }

    @FXML
    void btnReload(ActionEvent event) {
        SettingUpUserInfo();
        updateLabelWithTodayTotal();
        updateLabelWithTodayTotalOrders();
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
    void btnLogOutOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/LoginPage.fxml"));
        Parent root = loader.load();
        LoginPageController loginPageController = loader.getController();
        Stage currentStage = (Stage) lblTime.getScene().getWindow();
        currentStage.setScene(new Scene(root));
        currentStage.show();

    }

    private void updateLabelWithTodayTotal() {
        if (userEmail != null) {
            OrderServiceImpl orderService = ServiceFactory.getInstance().getServiceType(ServiceType.ORDER);
            UserServiceImpl userService = ServiceFactory.getInstance().getServiceType(ServiceType.FIRSTREGISTERUSER);
            UserModel user = userService.getUser(userEmail);
            String cashier = user.getUserName();
            Double todayTotal = orderService.getTodayTotalForUser(cashier);

            // Update the label
            lblTotal.setText(String.valueOf(todayTotal));
        }
    }

    private void updateLabelWithTodayTotalOrders() {
        if (userEmail != null) {
            OrderServiceImpl orderService = ServiceFactory.getInstance().getServiceType(ServiceType.ORDER);
            UserServiceImpl userService = ServiceFactory.getInstance().getServiceType(ServiceType.FIRSTREGISTERUSER);
            UserModel user = userService.getUser(userEmail);
            String cashier = user.getUserName();
            int todayTotalOrders = orderService.getTodayTotalOrdersForUser(cashier);

            // Update the lblTotalOrders label with today's total number of orders
            lblTotalOrders.setText(String.valueOf(todayTotalOrders));
        }
    }

    @FXML
    void btnOrdersOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/OrderPage.fxml"));
        Parent root = loader.load();
        OrderPageController orderPageController = loader.getController();
        orderPageController.settingUserEmail(userEmail);
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
    void btnToDoList(ActionEvent event) {

    }

    @FXML
    void btnUSerUpdate(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/ViewUserProfilePage.fxml"));
        Parent root = loader.load();
        ViewUserProfilePageController viewUserProfilePageController = loader.getController();
        viewUserProfilePageController.settingUserEmail(userEmail);
        Stage currentStage = (Stage) lblSlogan.getScene().getWindow();
        currentStage.setScene(new Scene(root));
        currentStage.show();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displaycurrentDateAndTime();
        displayTotalItems();
        loadSalesData();
        //lblActiveHours.setText("00:00");
        //SettingUpUserInfo();

    }

    private void displayTotalItems() {
        ItemServiceImpl itemService = ServiceFactory.getInstance().getServiceType(ServiceType.ITEM);
        long totalItems = itemService.getTotalItemsCount();
        lblTotalItems.setText(String.valueOf(totalItems));
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

    public void loadSalesData() {
        // Get the daily sales from the service
        OrderServiceImpl orderService = ServiceFactory.getInstance().getServiceType(ServiceType.ORDER);
        Map<String, Long> weeklyOrderCount = orderService.getWeeklyOrderCount();

        // Set labels for the graph axes
        xAxis.setLabel("Date");
        yAxis.setLabel("Total Orders");

        // Create a new series for the graph
        XYChart.Series<String, Number> orderSeries = new XYChart.Series<>();
        orderSeries.setName("Orders This Week");

        // Populate the series with data
        for (Map.Entry<String, Long> entry : weeklyOrderCount.entrySet()) {
            orderSeries.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        // Add the series to the BarChart
        Graph.getData().clear(); // Clear any existing data
        Graph.getData().add(orderSeries);
    }


}
