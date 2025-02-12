package wayfarer_user.config.jwt

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("jwt")
class JwtProperties {
    lateinit var secretKey: String
    var expiration: Expiration = Expiration()

    class Expiration {
        var accessTokenExpiration: Long = 0L
        var refreshTokenExpiration: Long = 0L
    }
}
