package com.gourav.payflowx.service.impl;

import com.gourav.payflowx.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import com.gourav.payflowx.dto.request.CreateUserRequest;
import com.gourav.payflowx.dto.response.UserResponse;
import com.gourav.payflowx.entity.User;
import com.gourav.payflowx.exception.ResourceAlreadyExistsException;
import com.gourav.payflowx.repository.UserRepository;
import com.gourav.payflowx.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse createUser(CreateUserRequest request) {
        log.info("Creating user with email {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("User creation failed. Email already exists: {}", request.getEmail());
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        if (request.getPhoneNumber() != null &&
                userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            log.warn("User creation failed. Phone number already exists: {}", request.getPhoneNumber());
            throw new ResourceAlreadyExistsException("Phone number already exists");
        }

        User user = userMapper.toEntity(request);
        user.setId(UUID.randomUUID());

        User savedUser = userRepository.save(user);
        log.info("User created successfully. UserId={}", savedUser.getId());

        return userMapper.toResponse(savedUser);
    }
}