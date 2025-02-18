package wayfarer_auth.jwt.dto

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)
