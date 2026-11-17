package org.example.gotogether.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.example.gotogether.enums.UserRole;


public record CreateUserRequest(
        @NotBlank(message = "full name is required")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message =  "Email invalid")
        String email,

        @NotBlank(message = "phone is required")
        String phone,

        @NotBlank(message = "Password is required")
        @Size(min = 8 , message = "password must contain 8 chars at least")
        String password,

        @NotBlank(message = "role is required")
        UserRole role,

        String permisNumber
) {
}
