package org.example.gotogether.service.UserService;

import org.example.gotogether.dto.request.CreateUserRequest;
import org.example.gotogether.dto.response.UserResponseDto;
import org.example.gotogether.entity.User;
import org.example.gotogether.enums.UserRole;
import org.example.gotogether.mapper.UserMapper;
import org.example.gotogether.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(
            UserRepository userRepository,
            UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponseDto createUser(CreateUserRequest request) {

        validateRole(request);
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("this email already exist ");
        }
        return userMapper.toResponse(userRepository.save(userMapper.toNewUser(request)));
    }

    @Override
    public UserResponseDto updateUser(CreateUserRequest request , Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                    new IllegalArgumentException("User not found")
                );
        validateRole(request);
        userMapper.updateExistingUser(user , request);

        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    public List<UserResponseDto> allUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    public User findUserById(Long id) {
        return getUser(id);
    }

    @Override
    public void delete(Long id) {
       User user = getUser(id);
       userRepository.delete(user);
    }

    @Override
    public void validateRole(CreateUserRequest request) {
        if (
                request.role() == UserRole.DRIVER && (
                        request.permisNumber() == null ||
                        request.permisNumber().isBlank())
        ) {
           throw new IllegalArgumentException("driver must have a permis number");
        }
    }

    private User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

}
