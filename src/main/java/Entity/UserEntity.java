package Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "user")
public class UserEntity {
    @Id
    private int userId;
    private String userName;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] imageData;
    private String email;
    private String password;
    private Double salary;
    @Enumerated(EnumType.STRING)
    private UserType userType;

}
