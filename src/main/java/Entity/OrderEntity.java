package Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import model.OrderDetail;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    private String orderId;
    private String customerName;
    private String customerEmail;
    private LocalDate orderDate;
    private LocalTime orderTime;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<OrderDetailEntity> orderDetails;
    private String cashier;
    private Double orderTotal;

}
