package controller;

import Entity.OrderDetailEntity;
import Entity.OrderEntity;
import Entity.UserType;
import Service.Custom.Impl.OrderServiceImpl;
import Service.Custom.UserService;
import Service.ServiceFactory;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.UserModel;
import util.ServiceType;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class OrderPageController implements Initializable {

    @FXML
    private TableColumn<?, ?> colCashier;

    @FXML
    private TableColumn<?, ?> colCost;

    @FXML
    private TableColumn<?, ?> colCustomerName;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colItems;

    @FXML
    private TableColumn<?, ?> colOrdrId;

    @FXML
    private TableColumn<?, ?> colItemName;

    @FXML
    private TableColumn<?, ?> colItemPrice;

    @FXML
    private TableView<OrderDetailEntity> itemTable;

    @FXML
    private TableColumn<?, ?> colQuantity;

    @FXML
    private DatePicker datepicker;

    @FXML
    private TableView<OrderEntity> fullOrderTable;

    @FXML
    private JFXTextField txtCashierName;

    @FXML
    private JFXTextField txtCustomerEmail;

    @FXML
    private JFXTextField txtCustomerName;

    @FXML
    private JFXTextArea txtItems;

    @FXML
    private JFXTextField txtTotalCost;

    @FXML
    private JFXTextField txtsearchOrder;

    private String userEmail;
    public void settingUserEmail(String email) {
        this.userEmail = email;
        //SettingUpUserInfo();

    }

    @FXML
    void btnSearch(ActionEvent event) {

    }

    @FXML
    void btnUpdate(ActionEvent event) {
        OrderEntity selectedOrder = fullOrderTable.getSelectionModel().getSelectedItem();

        if (selectedOrder != null) {
            // Get updated customer name and email from text fields
            String updatedCustomerName = txtCustomerName.getText();
            String updatedCustomerEmail = txtCustomerEmail.getText();

            // Validate the input (optional)
            if (updatedCustomerName.isEmpty() || updatedCustomerEmail.isEmpty()) {
                showAlert("Error", "Both fields must be filled out!", Alert.AlertType.ERROR);
                return;
            }
            if (isValidEmail(updatedCustomerEmail)) {

                // Update the selected order details
                selectedOrder.setCustomerName(updatedCustomerName);
                selectedOrder.setCustomerEmail(updatedCustomerEmail);

            // Call the service to update the order in the database
            OrderServiceImpl orderService = ServiceFactory.getInstance().getServiceType(ServiceType.ORDER);
            boolean isUpdated = orderService.updateOrder(selectedOrder); // Implement this method in your service

            if (isUpdated) {
                showAlert("Success", "Order updated successfully!", Alert.AlertType.INFORMATION);

                clearTable();
                loadTable(); // Reload the table data
            } else {
                showAlert("Error", "Failed to update the order.", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Error", "Please select an order to update.", Alert.AlertType.WARNING);
        }
    }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isValidEmail(String email) {
        // Simple regex for basic email validation
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    private ObservableList<OrderEntity> orderEntities = FXCollections.observableArrayList();
    private ObservableList<OrderDetailEntity> orderDetailEntityObservableList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colOrdrId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colCashier.setCellValueFactory(new PropertyValueFactory<>("cashier"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("orderTotal"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));

        loadTable();


        fullOrderTable.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                // Get the selected order ID
                String selectedOrderId = newValue.getOrderId();
                txtCustomerName.setText(newValue.getCustomerName());
                txtCustomerEmail.setText(newValue.getCustomerEmail());

                // Load the items for the selected order
                LoadItemTable(selectedOrderId);
            }
        }));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colItemPrice.setCellValueFactory(new PropertyValueFactory<>("itemTotalPrice"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("itemQty"));

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
            Stage currentStage = (Stage) txtCustomerEmail.getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.show();
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/NormalUserDashBoardPage.fxml"));
            Parent root = loader.load();
            NormalUserDashBoardPageController normalUserDashBoardPageController = loader.getController();
            normalUserDashBoardPageController.settingUserEmail(userEmail);
            Stage currentStage = (Stage) txtCustomerEmail.getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.show();

        }
    }




    public void loadTable() {
        OrderServiceImpl orderService = ServiceFactory.getInstance().getServiceType(ServiceType.ORDER);
        // Get all items from the service
        List<OrderEntity> orderEntityList = orderService.getAllOrders();

        //  all items to the ObservableList
        orderEntities.addAll(orderEntityList);

        // Set the items to the TableView
        fullOrderTable.setItems(orderEntities);
        //ItemServiceImpl itemService = ServiceFactory.getInstance().getServiceType(ServiceType.ITEM);
        //lblitemcode.setText(String.valueOf(itemService.fetchLastItemId()));

    }

    public  void clearTable() {
        orderEntities.clear();
    }

    public  void LoadItemTable(String orderId) {

        OrderServiceImpl orderService = ServiceFactory.getInstance().getServiceType(ServiceType.ORDER);
        List<OrderDetailEntity> orderDetailEntityList = orderService.getOrderDetailsByOrderId(orderId);

        //orderDetailEntityObservableList.addAll(orderDetailEntityList);
        //itemTable.setItems(orderDetailEntityObservableList);
        itemTable.getItems().clear();
        itemTable.getItems().addAll(orderDetailEntityList);

    }

}
