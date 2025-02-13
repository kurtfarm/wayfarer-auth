package wayfarer_auth.config.jwt

import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Service
import java.util.*

@Service
class TokenProvider(
    private val jwtProperties: JwtProperties
) {
    private val signKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.secretKey)) // access token secret key
    fun generateToken(userId: Long): String {
        val now = Date()
        val expiryDate = Date(now.time + jwtProperties.accessTokenExpiration)

        return makeToken(expiryDate, userId)
    }

    private fun makeToken(expiry: Date, userId: Long): String {
        val now = Date()

        return Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(signKey)
            .compact()
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
