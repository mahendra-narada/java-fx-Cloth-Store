package Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orderDetail")
public class OrderDetailEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "orderId")
    private OrderEntity order;

    @Id
    private String itemName;
    private Integer itemQty;
    private Double itemTotalPrice;

    public OrderDetailEntity(String itemName, Integer itemQty, Double itemTotalPrice) {
        this.itemName = itemName;
        this.itemQty = itemQty;
        this.itemTotalPrice = itemTotalPrice;
    }

}
