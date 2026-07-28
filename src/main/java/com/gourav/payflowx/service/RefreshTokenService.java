package com.gourav.payflowx.service;

import com.gourav.payflowx.entity.RefreshToken;
import com.gourav.payflowx.entity.User;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(User user);

    RefreshToken verifyRefreshToken(String token);

    RefreshToken rotateRefreshToken(RefreshToken refreshToken);

    void revokeRefreshToken(String token);
}