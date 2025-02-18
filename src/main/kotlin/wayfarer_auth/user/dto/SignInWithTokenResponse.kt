package wayfarer_auth.user.dto

import wayfarer_auth.jwt.dto.TokenResponse

class SignInWithTokenResponse(
    val email: String,
    val tokens: TokenResponse
)
