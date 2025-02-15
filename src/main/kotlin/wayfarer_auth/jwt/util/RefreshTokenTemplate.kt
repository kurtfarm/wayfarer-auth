package wayfarer_auth.jwt.util

import org.springframework.data.redis.core.RedisTemplate
import java.util.concurrent.TimeUnit

open class RefreshTokenTemplate(
    private val redisTemplate: RedisTemplate<String, Any>
) {
    fun getRefreshToken(key: String): String? =
        redisTemplate.opsForValue().get(key) as? String

    fun getRefreshExpire(key: String): Long? =
        redisTemplate.getExpire(key,TimeUnit.MILLISECONDS)

    fun saveRefreshToken(token: String, userId: Long, expiration: Long) {
        val key = "refreshToken:$userId" // 사용자별 키 관리
        redisTemplate.opsForValue().set(key, token, expiration, TimeUnit.MILLISECONDS)
    }

    fun deleteRefreshToken(key: String) {
        redisTemplate.delete(key)
    }
}
