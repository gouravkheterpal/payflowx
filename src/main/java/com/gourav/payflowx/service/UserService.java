package com.gourav.payflowx.service;

import com.gourav.payflowx.dto.request.CreateUserRequest;
import com.gourav.payflowx.dto.response.UserResponse;

public interface UserService {

    UserResponse createUser(CreateUserRequest request);

}