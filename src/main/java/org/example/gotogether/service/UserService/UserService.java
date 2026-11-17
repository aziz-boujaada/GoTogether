package org.example.gotogether.service.UserService;

import org.example.gotogether.dto.request.CreateUserRequest;
import org.example.gotogether.dto.response.UserResponseDto;
import org.example.gotogether.entity.User;
import org.example.gotogether.enums.UserRole;


import java.util.List;
import java.util.Optional;

public interface UserService {
    UserResponseDto createUser(CreateUserRequest request);
    UserResponseDto updateUser(CreateUserRequest request , Long id);
    List<UserResponseDto> allUsers();
    User findUserById(Long id);
    void delete(Long id);void validateRole(CreateUserRequest request);
}
