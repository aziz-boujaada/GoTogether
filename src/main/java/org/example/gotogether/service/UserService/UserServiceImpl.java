package org.example.gotogether.service.UserService;

import org.example.gotogether.dto.request.CreateUserRequest;
import org.example.gotogether.dto.response.UserResponseDto;
import org.example.gotogether.entity.User;
import org.example.gotogether.enums.UserRole;
import org.example.gotogether.exception.InvalidValueException;
import org.example.gotogether.exception.ResourceAlreadyExistException;
import org.example.gotogether.exception.ResourceNotFoundException;
import org.example.gotogether.mapper.UserMapper;
import org.example.gotogether.repository.DriverRepository;
import org.example.gotogether.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final DriverRepository driverRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(
            UserRepository userRepository,
            UserMapper userMapper,
            DriverRepository driverRepository
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.driverRepository = driverRepository;
    }

    @Override
    public UserResponseDto createUser(CreateUserRequest request) {

        validateRole(request);
        if (userRepository.existsByEmail(request.email())) {
            throw new ResourceAlreadyExistException("this email already exist ");
        }
        return userMapper.toResponse(userRepository.save(userMapper.toNewUser(request)));
    }

    @Override
    public UserResponseDto updateUser(CreateUserRequest request , Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException("User not found")
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
    public UserResponseDto findUserById(Long id) {
        return getUser(id);
    }

    @Override
    public void delete(Long id) {
       User user = userRepository.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("User not found"));
       userRepository.delete(user);
    }

    @Override
    public void validateRole(CreateUserRequest request) {
        if (
                request.role() == UserRole.DRIVER && (
                        request.permisNumber() == null ||
                        request.permisNumber().isBlank())
        ) {
           throw new InvalidValueException("driver must have a permis number");
        }

        if(driverRepository.existsByPermisNumber(request.permisNumber())){
            throw new ResourceAlreadyExistException(
                    "This permis number already exists"
            );
        }
    }

    private UserResponseDto getUser(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

}
