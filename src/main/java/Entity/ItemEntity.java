package Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "item")
public class ItemEntity {
    @Id
    private String item_code;
    private String item_name;
    private Double price;
    private String size;
    private String category;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] imageData;
    private int Quantity;
    private String supplierEmail;
    private String supplierName;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] SupplierImageData;


}
