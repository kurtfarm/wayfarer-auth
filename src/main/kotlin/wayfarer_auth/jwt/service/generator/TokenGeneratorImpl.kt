package wayfarer_auth.jwt.service.generator

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import wayfarer_auth.jwt.config.JwtProperties
import wayfarer_auth.jwt.dto.TokenResponse
import wayfarer_auth.jwt.repository.RefreshTokenRepository
import java.util.*

@Component
class TokenGeneratorImpl(
    private val jwtProperties: JwtProperties,
    private val refreshTokenRepository: RefreshTokenRepository
) : TokenGenerator {
    private val signKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.secretKey)) // access token secret key

    override fun generateAccessToken(userId: Long): String {
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

    override fun generateRefreshToken(userId: Long): String {
        val expiryDate = Date(Date().time + jwtProperties.refreshTokenExpiration)
        val refreshToken = UUID.randomUUID().toString()
        val key = "RT::${refreshToken}-v1"

        refreshTokenRepository.saveHash(key, refreshToken, userId, expiryDate.time);

        return refreshToken
    }

    override fun reissueRefreshToken(refreshToken: String): TokenResponse? { // 재발급 신청시 이전의 refresh token은 삭제 후 재발급
        val key = "RT::${refreshToken}-v1"

        return refreshTokenRepository.findHash(key, refreshToken)?.let { userId ->
            refreshTokenRepository.deleteHash(key, refreshToken)

            val newAccessToken = generateAccessToken(userId)
            val newRefreshToken = generateRefreshToken(userId)
            println(newRefreshToken)
            TokenResponse(
                accessToken = newAccessToken,
                refreshToken = newRefreshToken
            )
        }
    }

    override fun deleteRefreshToken(refreshToken: String){
        val key = "RT::${refreshToken}-v1"
        refreshTokenRepository.deleteHash(key,refreshToken);
    }
}
