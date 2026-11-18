package org.example.gotogether.service.UserService;

import org.example.gotogether.dto.request.CreateUserRequest;
import org.example.gotogether.dto.response.UserResponseDto;
import org.example.gotogether.entity.User;

import java.util.List;

public interface UserService {
    UserResponseDto createUser(CreateUserRequest request);
    UserResponseDto updateUser(CreateUserRequest request , Long id);
    List<UserResponseDto> allUsers();
    UserResponseDto findUserById(Long id);
    void delete(Long id);void validateRole(CreateUserRequest request);
}
