package wayfarer_auth.jwt.service.generator

import wayfarer_auth.jwt.dto.TokenResponse

interface TokenGenerator {
    fun generateAccessToken(userId: Long): String
    fun generateRefreshToken(userId: Long): String
    fun reissueRefreshToken(refreshToken: String): TokenResponse?
    fun deleteRefreshToken(refreshToken: String)
}
