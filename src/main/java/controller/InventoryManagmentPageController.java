package controller;

import Entity.ItemEntity;
import Entity.UserType;
import Service.Custom.Impl.ItemServiceImpl;
import Service.Custom.ItemService;
import Service.Custom.UserService;
import Service.ServiceFactory;
import com.jfoenix.controls.JFXTextField;
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
import model.Item;
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
import java.util.regex.Pattern;

public class InventoryManagmentPageController implements Initializable {

    @FXML
    private TableView<ItemEntity> ItemFullTable;

    @FXML
    private ImageView Itemimage;

    @FXML
    private ImageView SupplierImage;

    @FXML
    private TableColumn<?, ?> colImage;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private TableColumn<?, ?> colQuantity;

    @FXML
    private TableColumn<?, ?> colSize;

    @FXML
    private TableColumn<?, ?> colSupplier;

    @FXML
    private JFXTextField txtItemName;

    @FXML
    private JFXTextField txtItemPrice;

    @FXML
    private JFXTextField txtItemQuantity;

    @FXML
    private JFXTextField txtItemSize;

    @FXML
    private JFXTextField txtSearchBar;

    @FXML
    private JFXTextField txtSupplierEmail;

    @FXML
    private JFXTextField txtSupplierName;

    @FXML
    private JFXTextField txtitemCategory;

    @FXML
    private Label lblSuppliercode;

    @FXML
    private Label lblitemcode;

    @FXML
    private ImageView itemImageView;

    private ObservableList<ItemEntity> itemObservableList = FXCollections.observableArrayList();


    private String userEmail;
    private byte[] ItemimageBytes;
    private byte[] SupplierImageBytes;

    public void settingUserEmail(String email) {
        this.userEmail = email;
        //SettingUpUserInfo();
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
    void btnAddItem(ActionEvent event) {
        String itemName = txtItemName.getText();
        String priceText = txtItemPrice.getText();
        String itemSize = txtItemSize.getText();
        String quantityText = txtItemQuantity.getText();
        String category = txtitemCategory.getText();
        String supplierName = txtSupplierName.getText();
        String supplierEmail = txtSupplierEmail.getText();
        byte[] image = ItemimageBytes; // Assuming imageBytes is already populated
        byte[] supplierImage = SupplierImageBytes;
        //String itemCode = lblitemcode.getText(); // Item code

        if (itemName.isEmpty() || priceText.isEmpty() || itemSize.isEmpty() || quantityText.isEmpty() ||
                category.isEmpty() || supplierName.isEmpty() || supplierEmail.isEmpty() || image == null || supplierImage == null) {
            showAlert("Error", "All fields must be filled!", Alert.AlertType.ERROR);
            return;
        }

        // Validate price and quantity
        double price;
        int quantity;
        try {
            price = Double.parseDouble(priceText);
            quantity = Integer.parseInt(quantityText);
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid input for price or quantity!", Alert.AlertType.ERROR);
            return;
        }

        // Validate email format
        if (!isValidEmail(supplierEmail)) {
            showAlert("Error", "Invalid email format!", Alert.AlertType.ERROR);
            return;
        }

        // Create and add the item
        Item item = new Item(lblitemcode.getText(), itemName, price, itemSize, category, image, quantity, supplierEmail, supplierName, supplierImage);
        ItemService itemService = ServiceFactory.getInstance().getServiceType(ServiceType.ITEM);
        itemService.addItem(item);
        loadTable();
        showAlert("Success", "Item added successfully!", Alert.AlertType.INFORMATION);
    }

    @FXML
    void btnRemoveItem(ActionEvent event) {
        String itemcode = lblitemcode.getText();
        ItemService itemService = ServiceFactory.getInstance().getServiceType(ServiceType.ITEM);
        itemService.deleteItem(itemcode);
        showAlert("Success", "Item Deleted successfully!", Alert.AlertType.INFORMATION);
        loadTable();

    }

    @FXML
    void btnSearch(ActionEvent event) {
        String searchText = txtSearchBar.getText(); // Assuming there's a text field for the search

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
            ItemFullTable.getItems().clear();
            ItemFullTable.getItems().addAll(searchResults);
        }
    }

    @FXML
    void btnUpdateItem(ActionEvent event) {
        String itemName = txtItemName.getText();
        String priceText = txtItemPrice.getText();
        String itemSize = txtItemSize.getText();
        String quantityText = txtItemQuantity.getText();
        String category = txtitemCategory.getText();
        String supplierName = txtSupplierName.getText();
        String supplierEmail = txtSupplierEmail.getText();
        byte[] image = ItemimageBytes; // Assuming imageBytes is already populated
        byte[] supplierImage = SupplierImageBytes;
        //String itemCode = lblitemcode.getText(); // Item code

        if (itemName.isEmpty() || priceText.isEmpty() || itemSize.isEmpty() || quantityText.isEmpty() ||
                category.isEmpty() || supplierName.isEmpty() || supplierEmail.isEmpty() || image == null || supplierImage == null) {
            showAlert("Error", "All fields must be filled!", Alert.AlertType.ERROR);
            return;
        }

        // Validate price and quantity
        double price;
        int quantity;
        try {
            price = Double.parseDouble(priceText);
            quantity = Integer.parseInt(quantityText);
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid input for price or quantity!", Alert.AlertType.ERROR);
            return;
        }

        // Validate email format
        if (!isValidEmail(supplierEmail)) {
            showAlert("Error", "Invalid email format!", Alert.AlertType.ERROR);
            return;
        }

        Item item = new Item(lblitemcode.getText(), itemName, price, itemSize, category, image, quantity, supplierEmail, supplierName, supplierImage);

        ItemService itemService = ServiceFactory.getInstance().getServiceType(ServiceType.ITEM);
        itemService.updateItem(item);
        showAlert("Success", "Item Updated successfully!", Alert.AlertType.INFORMATION);
        loadTable();
    }

    @FXML
    void btnUploadImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File file = fileChooser.showOpenDialog(Itemimage.getScene().getWindow());
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            Itemimage.setImage(image);
            ItemimageBytes = convertImageToBytes(file);
        }
    }

    @FXML
    void btnUploadImageSupplier(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File file = fileChooser.showOpenDialog(Itemimage.getScene().getWindow());
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            SupplierImage.setImage(image);
            SupplierImageBytes = convertImageToBytes(file);
        }

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
        ItemFullTable.setItems(itemObservableList);
        //ItemServiceImpl itemService = ServiceFactory.getInstance().getServiceType(ServiceType.ITEM);
        lblitemcode.setText(String.valueOf(itemService.fetchLastItemId()));

    }

    @FXML
    void btnLoadTable(ActionEvent event) {
        txtSearchBar.setText("");
        txtItemName.setText("");
        txtSupplierEmail.setText("");
        txtitemCategory.setText("");
        txtItemSize.setText("");
        txtItemQuantity.setText("");
        txtItemPrice.setText("");
        ItemimageBytes = null;
        SupplierImageBytes = null;
        txtSupplierName.setText("");

// Reset the ImageView components (if you're using them)
        Itemimage.setImage(null); // Reset the item image
        SupplierImage.setImage(null);

        loadTable();
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        ItemServiceImpl itemService = ServiceFactory.getInstance().getServiceType(ServiceType.ITEM);
//        lblitemcode.setText(String.valueOf(itemService.fetchLastItemId()));

        colName.setCellValueFactory(new PropertyValueFactory<>("item_name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        colSupplier.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        loadTable();
        ItemFullTable.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldValue, newValue) -> {
            if(newValue!=null){
                setTextToValues(newValue);
                displayItemImage(newValue.getImageData(),newValue.getSupplierImageData());

            }

        }));

    }

    private void setTextToValues(ItemEntity newValue) {
        lblitemcode.setText(newValue.getItem_code());
        txtItemName.setText(newValue.getItem_name());
        txtItemPrice.setText(String.valueOf(newValue.getPrice()));
        txtItemQuantity.setText(String.valueOf(newValue.getQuantity()));
        txtitemCategory.setText(newValue.getCategory());
        txtItemSize.setText(newValue.getSize());
        txtSupplierName.setText(newValue.getSupplierName());
        txtSupplierEmail.setText(newValue.getSupplierEmail());

    }


    private void displayItemImage(byte[] imageData,byte[] supplierImaBytes) {
        if (imageData != null && imageData.length > 0) {
            // Convert byte[] to Image
            Image image = new Image(new ByteArrayInputStream(imageData));
            Image supimage = new Image(new ByteArrayInputStream(supplierImaBytes));
            //itemImageView.setImage(image);
            Itemimage.setImage(image);
            SupplierImage.setImage(supimage);

            //itemImageView.setFitHeight(200);  // Set desired height
            //itemImageView.setFitWidth(200);   // Set desired width
            //itemImageView.setPreserveRatio(true);
        } else {
            // If no image is available, clear the ImageView
            //itemImageView.setImage(null);
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    // Utility method to show alerts
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
            Stage currentStage = (Stage) txtSearchBar.getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.show();
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/NormalUserDashBoardPage.fxml"));
            Parent root = loader.load();
            NormalUserDashBoardPageController normalUserDashBoardPageController = loader.getController();
            normalUserDashBoardPageController.settingUserEmail(userEmail);
            Stage currentStage = (Stage) txtSearchBar.getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.show();

        }
    }
}