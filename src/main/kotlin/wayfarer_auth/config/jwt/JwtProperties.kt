package wayfarer_auth.config.jwt

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

@Configuration
class JwtProperties(
    @Value("\${jwt.secret}") val secretKey: String,
    @Value("\${jwt.expiration.access}") val accessTokenExpiration: Long,
    @Value("\${jwt.expiration.refresh}") val refreshRefreshExpiration: Long
)
