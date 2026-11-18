package org.example.gotogether.controller;


import org.example.gotogether.dto.request.CreateUserRequest;
import org.example.gotogether.dto.response.UserResponseDto;
import org.example.gotogether.entity.User;
import org.example.gotogether.service.UserService.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api/users")
public class UserController {


        private final UserService userService;

        public UserController(UserService userService) {
            this.userService = userService;
        }

        @PostMapping

        public ResponseEntity<UserResponseDto> createUser(
                @RequestBody CreateUserRequest request
        ) {
            UserResponseDto response = userService.createUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        @PutMapping("/{id}")
        public ResponseEntity<UserResponseDto> updateUser(
                @PathVariable Long id,
                @RequestBody CreateUserRequest request
        ) {
            UserResponseDto response = userService.updateUser(request, id);
            return ResponseEntity.ok(response);
        }

        @GetMapping

        public ResponseEntity<List<UserResponseDto>> getAllUsers() {
            return ResponseEntity.ok(userService.allUsers());
        }

        @GetMapping("/{id}")
        public ResponseEntity<UserResponseDto> getUserById(
                @PathVariable Long id
        ) {
            return ResponseEntity.ok(userService.findUserById(id));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteUser(
                @PathVariable Long id
        ) {
            userService.delete(id);
            return ResponseEntity.noContent().build();
        }
}
