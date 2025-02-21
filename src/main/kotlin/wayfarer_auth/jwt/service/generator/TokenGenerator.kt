package wayfarer_auth.jwt.service.generator

interface TokenGenerator {
    fun generateAccessToken(userId: Long): String
    fun generateRefreshToken(userId: Long): String
    fun reissueRefreshToken(refreshToken: String, userId: Long): String?
    fun deleteRefreshToken(refreshToken: String)
}
