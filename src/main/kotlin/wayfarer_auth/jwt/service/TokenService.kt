package wayfarer_auth.jwt.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Service
import wayfarer_auth.jwt.config.JwtProperties
import wayfarer_auth.jwt.repository.RefreshTokenRepository
import java.util.*

@Service
class TokenService(
    private val jwtProperties: JwtProperties,
    private val refreshTokenRepository: RefreshTokenRepository
) {
    private val signKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.secretKey)) // access token secret key
    fun generateAccessToken(userId: Long): String {
        val expiryDate = Date(Date().time + jwtProperties.accessTokenExpiration)

        return makeToken(expiryDate, userId)
    }

    private fun makeToken(expiry: Date, userId: Long): String =
        Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(Date())
            // .claim("nonce", UUID.randomUUID().toString()) // 랜덤 Claim 추가
            .setExpiration(expiry)
            .signWith(signKey)
            .compact()

    fun generateRefreshToken(userId: Long): String {
        val expiryDate = Date(Date().time + jwtProperties.refreshTokenExpiration)
        val refreshToken = UUID.randomUUID().toString()
        val key = "RT::${refreshToken}-v1"

        refreshTokenRepository.saveHash(key, refreshToken, userId, expiryDate.time);

        return refreshToken
    }

    fun isValidStoredRefreshToken(refreshToken: String): Boolean {
        val key = "RT::${refreshToken}-v1"
        val userId = refreshTokenRepository.findHash(key, refreshToken);
        return userId != null
    }

    fun reissueRefreshToken(refreshToken: String, userId: Long): String? { // 재발급 신청시 이전의 refresh token은 삭제 후 재발급
        val key = "RT::${refreshToken}-v1"

        refreshTokenRepository.deleteHash(key, refreshToken);
        val newRefreshToken = generateRefreshToken(userId);

        return newRefreshToken
    }
//    fun validateAccessToken(token: String): Boolean {
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

    fun getAuthentication(token: String): UsernamePasswordAuthenticationToken {
        val claims: Claims = getAccessTokenClaims(token)
        val userId = claims.subject ?: throw RuntimeException("Invalid token")

        // 권한 정보 추출
        val authorities = listOf(SimpleGrantedAuthority("ROLE_USER"))
        val principal = User(userId, "", authorities)

        return UsernamePasswordAuthenticationToken(principal, token, authorities)
    }

    private fun getAccessTokenClaims(token: String): Claims =
        Jwts.parserBuilder()
            .setSigningKey(signKey)
            .build()
            .parseClaimsJws(token)
            .body

}
