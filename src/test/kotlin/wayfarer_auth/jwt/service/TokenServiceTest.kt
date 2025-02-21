package wayfarer_auth.jwt.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.RedisTemplate
import wayfarer_auth.jwt.repository.RefreshTokenRepository
import java.util.*
import java.util.concurrent.TimeUnit

@SpringBootTest
class TokenServiceTest {

    @Autowired
    private lateinit var tokenService: TokenService

    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, Any>

    @Autowired
    private lateinit var refreshTokenRepository: RefreshTokenRepository

    @Test
    fun `유저의 아이디를 전달해 Access Token을 만들 수 있다`() {
        // Given
        val userId = 1L

        // When
        val token = tokenService.generateAccessToken(userId)

        // Then
        assertThat(token).isNotNull()
        assertThat(token).isNotEmpty()
        println("Generated Token: $token")
    }

    //    @Test
    //    fun `만료된 토큰은 유효성 검증에 실패한다`() {
    //        // Given
    //        val expiredToken = generateExpiredToken()
    //
    //        // When & Then
    //        val exception = assertThrows(ExpiredJwtException::class.java) {
    //            tokenProvider.validateAccessToken(expiredToken)
    //        }
    //        assertNotNull(exception)
    //        println("Expired Token Exception: ${exception.message}")
    //    }

//    @Test
//    fun `Access Token 기반으로 인증 정보를 가져올 수 있다`() {
//        // Given
//        val userId = 1L
//        val token = tokenService.generateAccessToken(userId)
//
//        // When
//        val authentication = tokenService.getAuthentication(token)
//
//        // Then
//        assertThat(authentication).isNotNull()
//        assertThat(authentication.name).isEqualTo(userId.toString())
//        assertThat(authentication.authorities).isNotEmpty()
//    }

    @Test
    fun `유저의 아이디를 전달해 Refresh Token을 만들고 Redis에 저장할 수 있다`() {
        // Given
        val userId = 1L
        val refreshToken = tokenService.generateRefreshToken(userId)
        val key = "RT::$refreshToken-v1"
        val expiryDate = Date(Date().time + 604800000)

        // When
        refreshTokenRepository.saveHash(key, refreshToken, userId, expiryDate.time)
        val storedUserId = refreshTokenRepository.findHash(key, refreshToken)
        println(storedUserId)
        val ttl = refreshTokenRepository.getExpire(key)

        // Then
        assertThat(refreshToken).isNotNull
        assertThat(storedUserId).isEqualTo(userId)
        assertThat(ttl).isGreaterThan(0)
        println("Generated Refresh Token: $refreshToken")
        println("Token TTL: $ttl milliseconds")
    }

    @Test
    fun `Refresh Token이 만료되었을 때 새 토큰이 재발급된다`() {
        // Given
        val userId = 1L
        val refreshToken = tokenService.generateRefreshToken(userId)
        val key = "RT::$refreshToken-v1"

        // When
        redisTemplate.expire(key, 1, TimeUnit.MILLISECONDS)
        Thread.sleep(10)

        if (tokenService.isValidStoredRefreshToken(refreshToken)){
            refreshTokenRepository.deleteHash(key, refreshToken)
        }

        val newRefreshToken = tokenService.reissueRefreshToken(refreshToken, userId)

        // Then
        assertThat(newRefreshToken).isNotNull
        assertThat(newRefreshToken).isNotEqualTo(refreshToken)
        println("Reissued Refresh Token: $newRefreshToken")
    }

    //    private fun generateExpiredToken(): String {
    //        val now = Date()
    //        val expiredDate = Date(now.time - 1000)
    //        return Jwts.builder()
    //            .setSubject("expiredUser")
    //            .setIssuedAt(now)
    //            .setExpiration(expiredDate)
    //            .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.secretKey)))
    //            .compact()
    //    }
}
