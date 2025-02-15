package wayfarer_auth.jwt.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Service
import wayfarer_auth.jwt.config.JwtProperties
import java.util.*
import java.util.concurrent.TimeUnit

@Service
class TokenService(
    private val jwtProperties: JwtProperties,
    private val redisTemplate: RedisTemplate<String, Any>
) {
    private val signKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.secretKey)) // access token secret key
    fun generateAccessToken(userId: Long): String {
        val now = Date()
        val expiryDate = Date(now.time + jwtProperties.accessTokenExpiration)

        return makeToken(expiryDate, userId, "ACCESS")
    }

    fun generateRefreshToken(userId: Long): String {
        val now = Date()
        val expiryDate = Date(now.time + jwtProperties.refreshTokenExpiration)
        val refreshToken = makeToken(expiryDate, userId, "REFRESH")

        saveRefreshToken(refreshToken, userId)

        return refreshToken
    }

    private fun makeToken(expiry: Date, userId: Long, tokenType: String): String =
        Jwts.builder()
            .setSubject(userId.toString())
            .claim("type", tokenType)
            .setIssuedAt(Date())
            .setExpiration(expiry)
            .signWith(signKey)
            .compact()

    fun validateStoredRefreshToken(userId: Long, token: String): Boolean {
        val key = "refreshToken:$userId"
        val storedToken = getRefreshToken(key)
        return storedToken == token
    }

    fun reissueRefreshTokenIfExpired(userId: Long, token: String): String? { // 기간이 만료된 경우 이전의 refresh token은 삭제 후 재발급
        val key = "refreshToken:$userId"

        if (validateStoredRefreshToken(userId, token)) { // RTR
            val ttl = redisTemplate.getExpire(key, TimeUnit.MILLISECONDS) ?: 0
            if (ttl <= 0) {
                deleteRefreshToken(key)
                val newRefreshToken = generateRefreshToken(userId)
                return newRefreshToken
            }
        }
        return null
    }

    fun getRefreshToken(key: String): String? =
        redisTemplate.opsForValue().get(key) as? String


    fun saveRefreshToken(token: String, userId: Long) {
        val key = "refreshToken:$userId" // 사용자별 키 관리
        redisTemplate.opsForValue().set(key, token, jwtProperties.refreshTokenExpiration, TimeUnit.MILLISECONDS)
    }

    fun deleteRefreshToken(key: String) {
        redisTemplate.delete(key)
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
