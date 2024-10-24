package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class AdminDashBoardPageController {

    @FXML
    private BarChart<?, ?> Graph;

    @FXML
    private ImageView UserImageId;

    @FXML
    private Rectangle lblActiveHours1;

    @FXML
    private Rectangle lblActiveHours11;

    @FXML
    private Rectangle lblActiveHours111;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblOrders;

    @FXML
    private Label lblSlogan;

    @FXML
    private Label lblTime;

    @FXML
    private Label lblTopCashier;

    @FXML
    private Label lblTopItem;

    @FXML
    private Rectangle lblTotal;

    @FXML
    private Label lblTotalItems;

    @FXML
    private Rectangle lblTotalOrders;

    private String userEmail;

    public void settingUserEmail(String email) {
        this.userEmail = email;
    }

    @FXML
    void btnCall(ActionEvent event) {

    }

    @FXML
    void btnInventoryMangeOnAction(ActionEvent event) {

    }

    @FXML
    void btnLogOutOnAction(ActionEvent event) {

    }

    @FXML
    void btnOrdersOnAction(ActionEvent event) {

    }

    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) {

    }

    @FXML
    void btnReportOnAction(ActionEvent event) {

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

    @FXML
    void btnUsersOnAction(ActionEvent event) {

    }

}
