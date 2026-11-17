package org.example.gotogether.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.example.gotogether.entity.Driver;
import org.example.gotogether.entity.User;
import org.example.gotogether.enums.UserRole;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
    private UserRole role;
    private String phone;
    private String permisNumber;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();

        if (user instanceof Driver driver) {
            this.permisNumber = driver.getPermisNumber();
        }

    }
}
