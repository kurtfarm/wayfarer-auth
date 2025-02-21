package wayfarer_auth.jwt.service.validator

interface TokenValidator {
    fun isValidStoredRefreshToken(refreshToken: String): Boolean

//    fun validateAccessToken(token: String): Boolean
}
