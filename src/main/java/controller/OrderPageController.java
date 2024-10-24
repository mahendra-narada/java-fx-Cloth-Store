package controller;

import Entity.ItemEntity;
import Entity.OrderEntity;
import Service.Custom.Impl.ItemServiceImpl;
import Service.Custom.Impl.OrderServiceImpl;
import Service.ServiceFactory;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import util.ServiceType;

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

    @FXML
    void btnDelete(ActionEvent event) {

    }

    @FXML
    void btnReturn(ActionEvent event) {

    }

    @FXML
    void btnSearch(ActionEvent event) {

    }

    @FXML
    void btnUpdate(ActionEvent event) {

    }

    private ObservableList<OrderEntity> orderEntities = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colOrdrId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colCashier.setCellValueFactory(new PropertyValueFactory<>("cashier"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("orderTotal"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));


        loadTable();
        fullOrderTable.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldValue, newValue) -> {
            if(newValue!=null){
                //setTextToValues(newValue);
                //displayItemImage(newValue.getImageData(),newValue.getSupplierImageData());

            }

        }));
    }

    public void loadTable() {
        OrderServiceImpl orderService = ServiceFactory.getInstance().getServiceType(ServiceType.ORDER);
        // Get all items from the service
        List<OrderEntity> orderEntityList = orderService.getAllOrders();

        // Clear existing items in case of reloading
        //orderEntities.clear();

        // Add all items to the ObservableList
        orderEntities.addAll(orderEntityList);

        // Set the items to the TableView
        fullOrderTable.setItems(orderEntities);
        //ItemServiceImpl itemService = ServiceFactory.getInstance().getServiceType(ServiceType.ITEM);
        //lblitemcode.setText(String.valueOf(itemService.fetchLastItemId()));

    }
}
