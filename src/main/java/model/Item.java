package model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Item {

    private String item_code;
    private String item_name;
    private Double price;
    private String size;
    private String category;
    private byte[] imageData;
    private int Quantity;
    private String supplierEmail;
    private String supplierName;
    private byte[] SupplierImageData;
}
