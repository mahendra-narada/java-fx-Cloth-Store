package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders {
    private String orderId;
    private String customerName;
    private String customerEmail;
    private LocalDate orderDate;
    private LocalTime orderTime;
    private List<OrderDetail> orderDetails;
    private String cashier;
    private Double orderTotal;

    public Orders(String orderId, String customerName, String customerEmail, LocalDate orderDate, LocalTime orderTime, Double orderTotal,String cashier) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.orderDate = orderDate;
        this.orderTime=orderTime;
        this.orderTotal = orderTotal;
        this.cashier = cashier;
    }

}
