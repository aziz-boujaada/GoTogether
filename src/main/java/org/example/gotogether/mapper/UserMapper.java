package org.example.gotogether.mapper;


import org.example.gotogether.dto.request.CreateUserRequest;
import org.example.gotogether.dto.response.UserResponseDto;
import org.example.gotogether.entity.Driver;
import org.example.gotogether.entity.Passenger;
import org.example.gotogether.entity.User;
import org.springframework.stereotype.Component;

@Component
    public class UserMapper {

        public User toNewUser(CreateUserRequest request) {
            User user = switch (request.role()) {
                case DRIVER -> new Driver();
                case PASSENGER -> new Passenger();
            };

            updateUser(user, request);
            user.setActive(true);
            return user;
        }

        public void updateUser(User user, CreateUserRequest request) {
            user.setName(request.name());
            user.setEmail(request.email());
            user.setPassword(request.password());
            user.setRole(request.role());

            if (user instanceof Driver driver && request.permisNumber() != null) {
                driver.setPermisNumber(request.permisNumber());
            }

        }

        public void updateExistingUser(User user, CreateUserRequest request) {
            updateUser(user, request);
        }

        public UserResponseDto toResponse(User user) {
            return new UserResponseDto(user);
        }
}
