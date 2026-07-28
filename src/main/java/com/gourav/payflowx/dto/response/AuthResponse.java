package com.gourav.payflowx.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    /**
     * Short-lived JWT (e.g. 15 minutes)
     */
    private String accessToken;

    /**
     * Long-lived token (e.g. 7 days)
     */
    private String refreshToken;

    /**
     * Token type used in Authorization header
     */
    @Builder.Default
    private String tokenType = "Bearer";
}