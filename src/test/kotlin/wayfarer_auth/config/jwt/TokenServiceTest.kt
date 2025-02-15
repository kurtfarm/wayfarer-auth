package wayfarer_auth.config.jwt

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import wayfarer_auth.jwt.config.JwtProperties
import wayfarer_auth.jwt.service.TokenService

@SpringBootTest
class TokenServiceTest {

    @Autowired
    private lateinit var tokenProvider: TokenService

    @Autowired
    private lateinit var jwtProperties: JwtProperties

    @BeforeEach
    fun setUp() {
        tokenProvider = TokenService(jwtProperties)
    }

    @Test
    fun `유저의 아이디를 전달해 토큰을 만들 수 있다`() {
        // Given
        val userId = 1L

        // When
        val token = tokenProvider.generateToken(userId)

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

    @Test
    fun `토큰 기반으로 인증 정보를 가져올 수 있다`() {
        // Given
        val userId = 1L
        val token = tokenProvider.generateToken(userId)

        // When
        val authentication = tokenProvider.getAuthentication(token)

        // Then
        assertThat(authentication).isNotNull()
        assertThat(authentication.name).isEqualTo(userId.toString())
        assertThat(authentication.authorities).isNotEmpty()
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
