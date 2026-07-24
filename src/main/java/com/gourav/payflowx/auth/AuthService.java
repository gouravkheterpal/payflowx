package com.gourav.payflowx.auth;

import com.gourav.payflowx.dto.response.AuthResponse;
import com.gourav.payflowx.dto.request.LoginRequest;
import com.gourav.payflowx.dto.request.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}