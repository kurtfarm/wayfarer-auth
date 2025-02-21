package wayfarer_auth.jwt.service.validator

import org.springframework.stereotype.Component
import wayfarer_auth.jwt.repository.RefreshTokenRepository

@Component
class TokenValidatorImpl(
    private val refreshTokenRepository: RefreshTokenRepository
) : TokenValidator {
    override fun isValidStoredRefreshToken(refreshToken: String): Boolean {
        val key = "RT::${refreshToken}-v1"
        val userId = refreshTokenRepository.findHash(key, refreshToken);
        return userId != null
    }

//    override fun validateAccessToken(token: String): Boolean {
//        try {
//            getAccessTokenClaims(token)
//            return true
//        } catch (e: Exception) {
//            when (e) {
//                is SecurityException -> {}  // Invalid JWT Token
//                is MalformedJwtException -> {}  // Invalid JWT Token
//                is ExpiredJwtException -> {}    // Expired JWT Token
//                is UnsupportedJwtException -> {}    // Unsupported JWT Token
//                is IllegalArgumentException -> {}   // JWT claims string is empty
//                else -> {}
//            }
//            throw e
//        }
//    }
//
//    private fun getAccessTokenClaims(token: String): Claims =
//        Jwts.parserBuilder()
//            .setSigningKey(signKey)
//            .build()
//            .parseClaimsJws(token)
//            .body

//    fun getAuthentication(token: String): UsernamePasswordAuthenticationToken {
//        val claims: Claims = getAccessTokenClaims(token)
//        val userId = claims.subject ?: throw RuntimeException("Invalid token")
//
//        // 권한 정보 추출
//        val authorities = listOf(SimpleGrantedAuthority("ROLE_USER"))
//        val principal = User(userId, "", authorities)
//
//        return UsernamePasswordAuthenticationToken(principal, token, authorities)
//    }
}
