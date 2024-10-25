package model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItem {
    private String itemName;
    private double price;
    private int quantity;
    private double total;
}
