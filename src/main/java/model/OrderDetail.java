package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {

    private Orders order;
    private String itemId;
    private Integer itemQty;
    private Double itemTotalPrice;


    public OrderDetail(String itemId, Integer itemQty, Double itemTotalPrice) {
        this.itemId = itemId;
        this.itemQty = itemQty;
        this.itemTotalPrice = itemTotalPrice;
    }

}
