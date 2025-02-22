package wayfarer_auth.jwt.service

import org.springframework.stereotype.Service
import wayfarer_auth.jwt.dto.TokenResponse
import wayfarer_auth.jwt.service.generator.TokenGenerator
import wayfarer_auth.jwt.service.validator.TokenValidator

@Service
class TokenService(
    private val tokenGenerator: TokenGenerator,
    private val tokenValidator: TokenValidator
) {
    fun generateAccessToken(userId: Long): String {
        return tokenGenerator.generateAccessToken(userId)
    }

    fun generateRefreshToken(userId: Long): String {
        return tokenGenerator.generateRefreshToken(userId)
    }

    fun reissueRefreshToken(refreshToken: String): TokenResponse? {
        if (!isValidStoredRefreshToken(refreshToken)){
            throw IllegalArgumentException("Invalid Refresh Token")
        }
        return tokenGenerator.reissueRefreshToken(refreshToken)
    }

    fun isValidStoredRefreshToken(refreshToken: String): Boolean {
        return tokenValidator.isValidStoredRefreshToken(refreshToken)
    }

    fun deleteRefreshToken(refreshToken: String) {
        return tokenGenerator.deleteRefreshToken(refreshToken)
    }
}
