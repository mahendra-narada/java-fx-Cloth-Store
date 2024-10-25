package controller;

import Entity.ItemEntity;
import Entity.UserType;
import Service.Custom.Impl.ItemServiceImpl;
import Service.Custom.Impl.OrderServiceImpl;
import Service.Custom.Impl.UserServiceImpl;
import Service.Custom.OrderService;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.CartItem;
import model.Item;
import model.UserModel;
import util.ServiceType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PlaceOrderPageController implements Initializable {

    @FXML
    private TableView<ItemEntity> Itemtable;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private TableColumn<?, ?> colQuantity;

    @FXML
    private ImageView itemImage;

    @FXML
    private JFXTextField txtDiscount;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtFullName;

    @FXML
    private JFXTextArea txtItemCart;

    @FXML
    private TextArea txtQuantity;

    @FXML
    private JFXTextField txtSearchbar;

    @FXML
    private TableView<CartItem> cartTable;

    @FXML
    private TableColumn<?, ?> colCartPrice;

    @FXML
    private TableColumn<?, ?> colCartQuantity;

    @FXML
    private TableColumn<?, ?> colcartName;

    @FXML
    private TableColumn<?, ?> colItemTotal;

    @FXML
    private Label txtTotal;
    @FXML
    private Label lblitemprice;
    @FXML
    private Label txtItemName;

    @FXML
    private JFXTextField txtUserPyment;

    private String userEmail;
    private byte[] ItemimageBytes;

    private ObservableList<ItemEntity> itemObservableList = FXCollections.observableArrayList();
    private ObservableList<CartItem> itemCartObservableList = FXCollections.observableArrayList();

    public void settingUserEmail(String email) {
        this.userEmail = email;
        //SettingUpUserInfo();
    }

    @FXML
    void btnAddToCart(ActionEvent event) {
        int availableQuantity = getSelectedItemQuantity();
        String quantityText = txtQuantity.getText();

        // Check if the entered quantity is a valid integer
        if (quantityText.isEmpty()) {
            showAlert("Error", "Please enter a quantity!", Alert.AlertType.ERROR);
            return;
        }

        int enteredQuantity;
        try {
            enteredQuantity = Integer.parseInt(quantityText);
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid quantity!", Alert.AlertType.ERROR);
            return;
        }

        // Check if entered quantity is less than or equal to available quantity
        if (enteredQuantity > availableQuantity) {
            showAlert("Error", "Entered quantity exceeds available quantity!", Alert.AlertType.ERROR);
            return;
        }

        double itemtotalprice = (Double.parseDouble((lblitemprice.getText())) * enteredQuantity);
        // Add item name to the cart if the quantity is valid
        updateCartDisplay(txtItemName.getText(), enteredQuantity,lblitemprice.getText(),itemtotalprice);
    }

    private void updateCartDisplay(String itemName, int quantity, String price,double itemtotalprice) {

        double itemprice = Double.parseDouble((price));
        CartItem cartItem = new CartItem(itemName,itemprice,quantity,itemtotalprice);


        // Clear existing items in case of reloading
         //itemCartObservableList.clear();

        // Add all items to the ObservableList
        itemCartObservableList.add(cartItem);

        // Set the items to the TableView
        cartTable.setItems(itemCartObservableList);
        calculateTotal();
    }

    void calculateTotal() {
        // Variable to hold the total price
        double totalPrice = 0.0;

        // Iterate through each CartItem in the list and calculate the total price
        for (CartItem item : itemCartObservableList) {
            totalPrice += item.getPrice() * item.getQuantity();
        }

        // Set the total price to the txtTotal TextField (formatted as a string)
        txtTotal.setText(String.format("%.2f", totalPrice));
    }

    private int getSelectedItemQuantity() {
        ItemEntity selectedItem = Itemtable.getSelectionModel().getSelectedItem();
        return selectedItem != null ? selectedItem.getQuantity() : 0;
    }

    @FXML
    void btnCheck(ActionEvent event) {

    }

    @FXML
    void btnClearCart(ActionEvent event) {
        itemCartObservableList.clear();
    }

    @FXML
    void btnPlaceOrder(ActionEvent event) {

        String customerName = txtFullName.getText();
        String customerEmail = txtEmail.getText();

        if (customerName.isEmpty() || customerEmail.isEmpty()) {
            showAlert("Error", "Customer details must be provided!", Alert.AlertType.ERROR);
            return;
        }

        if (itemCartObservableList.isEmpty()) {
            showAlert("Error", "The cart is empty. Please add items!", Alert.AlertType.ERROR);
            return;
        }
        if (isValidEmail(customerEmail)) {
            // Call the service to place the order
            OrderServiceImpl orderService = ServiceFactory.getInstance().getServiceType(ServiceType.ORDER);
            String orderId = String.valueOf(orderService.fetchLastItemId());
            UserServiceImpl userService = ServiceFactory.getInstance().getServiceType(ServiceType.FIRSTREGISTERUSER);
            UserModel user = userService.getUser(userEmail);
            String cashier = user.getUserName();
            boolean isOrderPlaced = orderService.placeOrder(orderId,customerName, customerEmail, itemCartObservableList,cashier);

            if (isOrderPlaced) {
                showAlert("Success", "Order placed successfully!", Alert.AlertType.INFORMATION);
                List<String> orderedItems = new ArrayList<>();
                double totalAmount = 0;

                for (CartItem item : itemCartObservableList) {
                    orderedItems.add(item.getItemName() + " - " + item.getQuantity());
                    totalAmount += item.getPrice() * item.getQuantity();
                }
                OTPSender.sendOrderConfirmationEmail(
                        customerEmail,
                        customerName,
                        String.valueOf(orderService.fetchLastItemId()), // Assuming you have a way to get the latest order ID
                        orderedItems,
                        totalAmount,
                        cashier
                );
                showAlert("Success", "Email Sended successfully!", Alert.AlertType.INFORMATION);
                itemCartObservableList.clear();
            } else {
                showAlert("Error", "Failed to place the order.", Alert.AlertType.ERROR);
            }
        }else {
            showAlert("Error", "Please Input valid Email", Alert.AlertType.ERROR);

        }
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {

        String searchText = txtSearchbar.getText(); // Assuming there's a text field for the search

        if (searchText.isEmpty()) {
            showAlert("Error", "Please enter an item or supplier name to search!", Alert.AlertType.ERROR);
            return;
        }

        ItemServiceImpl itemService = ServiceFactory.getInstance().getServiceType(ServiceType.ITEM);
        List<ItemEntity> searchResults = itemService.searchItems(searchText);

        if (searchResults.isEmpty()) {
            showAlert("Info", "No items found!", Alert.AlertType.INFORMATION);
        } else {
            // Clear the table and display the search results
            Itemtable.getItems().clear();
            Itemtable.getItems().addAll(searchResults);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colName.setCellValueFactory(new PropertyValueFactory<>("item_name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        loadTable();
        Itemtable.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldValue, newValue) -> {
            if(newValue!=null){
                setTextToValues(newValue);
                displayItemImage(newValue.getImageData());

            }

        }));

        colcartName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colCartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colCartQuantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        colItemTotal.setCellValueFactory(new PropertyValueFactory<>("total"));


    }



    public void loadTable() {
        ItemServiceImpl itemService = ServiceFactory.getInstance().getServiceType(ServiceType.ITEM);

        // Get all items from the service
        List<ItemEntity> itemList = itemService.getAllItems();

        // Clear existing items in case of reloading
        itemObservableList.clear();

        // Add all items to the ObservableList
        itemObservableList.addAll(itemList);

        // Set the items to the TableView
        Itemtable.setItems(itemObservableList);
        //ItemServiceImpl itemService = ServiceFactory.getInstance().getServiceType(ServiceType.ITEM);


    }
    private void displayItemImage(byte[] imageData) {
        if (imageData != null && imageData.length > 0) {
            // Convert byte[] to Image
            Image image = new Image(new ByteArrayInputStream(imageData));
            itemImage.setImage(image);


            itemImage.setFitHeight(200);  // Set desired height
            itemImage.setFitWidth(200);   // Set desired width
            itemImage.setPreserveRatio(true);
        } else {
            // If no image is available, clear the ImageView
            itemImage.setImage(null);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void setTextToValues(ItemEntity newValue) {
       txtItemName.setText(newValue.getItem_name());
       lblitemprice.setText(String.valueOf(newValue.getPrice()));

    }

    private boolean isValidEmail(String email) {
        // Simple regex for basic email validation
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    @FXML
    void btnRemoveItem(ActionEvent event) {

        CartItem selectedItem = cartTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            double total = Double.parseDouble(txtTotal.getText());
            double NewTotal = total-selectedItem.getTotal();
            txtTotal.setText(String.valueOf(NewTotal));
            // Remove the selected item from the observable list
            itemCartObservableList.remove(selectedItem);

            // Refresh the cart table
            cartTable.refresh();
        } else {
            showAlert("Error", "Please select an item to remove.", Alert.AlertType.WARNING);
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
            Stage currentStage = (Stage) txtTotal.getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.show();
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/NormalUserDashBoardPage.fxml"));
            Parent root = loader.load();
            NormalUserDashBoardPageController normalUserDashBoardPageController = loader.getController();
            normalUserDashBoardPageController.settingUserEmail(userEmail);
            Stage currentStage = (Stage) txtTotal.getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.show();

        }
    }



}
