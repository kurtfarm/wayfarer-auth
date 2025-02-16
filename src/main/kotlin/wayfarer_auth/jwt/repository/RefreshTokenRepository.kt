package wayfarer_auth.jwt.repository

import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.*
import java.util.concurrent.TimeUnit

@Repository
class RefreshTokenRepository(
    private val redisTemplate: RedisTemplate<String, Any>,
    private val hashOperations: HashOperations<String, String, Any>
) {
    fun findHash(key: String, field: String): Long? {
        val value = hashOperations.get(key, field) as? String
        return value?.toLongOrNull()
    }

    fun saveHash(key: String, field: String, value: Long, expiryDate: Long) {
        hashOperations.put(key, field, value.toString())
        redisTemplate.expire(key, expiryDate, TimeUnit.MILLISECONDS) // 만료시간 설정
    }

    fun deleteHash(key: String, field: String) {
        hashOperations.delete(key, field)
    }

    fun getExpire(key: String): Long?{
        val ttl = redisTemplate.getExpire(key, TimeUnit.MILLISECONDS)
        return ttl ?: -2
    }
}
