package model;

import Entity.UserType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserModel {
    private int userId;
    private String userName;
    private byte[] imageData;
    private String email;
    private String password;
    private Double salary;
    private UserType userType;

}
