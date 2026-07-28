package com.gourav.payflowx.auth;

import com.gourav.payflowx.dto.request.LoginRequest;
import com.gourav.payflowx.dto.request.RefreshTokenRequest;
import com.gourav.payflowx.dto.request.RegisterRequest;
import com.gourav.payflowx.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refreshToken(RefreshTokenRequest request);

    void logout(RefreshTokenRequest request);
}