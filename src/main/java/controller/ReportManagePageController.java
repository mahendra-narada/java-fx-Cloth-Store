package controller;

import Entity.UserType;
import Service.Custom.Impl.OrderServiceImpl;
import Service.Custom.UserService;
import Service.ServiceFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.*;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import model.UserModel;
import util.ServiceType;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;


public class ReportManagePageController  implements Initializable {

    @FXML
    private BarChart<String, Number> Graph;

    private String userEmail;

    public void settingUserEmail(String email) {
        this.userEmail = email;
        //SettingUpUserInfo();
    }

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    void btnAnnulay(ActionEvent event) {

    }

    @FXML
    void btnDaily(ActionEvent event) {

    }

    @FXML
    void btnGenerate(ActionEvent event) {



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
            Stage currentStage = (Stage) xAxis.getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.show();
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/NormalUserDashBoardPage.fxml"));
            Parent root = loader.load();
            NormalUserDashBoardPageController normalUserDashBoardPageController = loader.getController();
            normalUserDashBoardPageController.settingUserEmail(userEmail);
            Stage currentStage = (Stage) xAxis.getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.show();

        }
    }

    @FXML
    void btnMonthly(ActionEvent event) {

    }

    @FXML
    void btnWeeklyOnAction(ActionEvent event) {

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadSalesData();
    }


}
