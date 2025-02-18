package wayfarer_auth.user.dto

import wayfarer_auth.jwt.dto.TokenResponse

data class SignUpWithTokenResponse(
    val user: SignUpResponse,
    val tokens: TokenResponse
)
