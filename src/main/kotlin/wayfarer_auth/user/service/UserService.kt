package wayfarer_auth.user.service

import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import wayfarer_auth.user.dto.SignUpResponse
import wayfarer_auth.user.entity.User
import wayfarer_auth.user.repository.UserRepository

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    @Transactional
    fun register(req: SignUpResponse): User {
        val user = User(
            email = req.email,
            pw = passwordEncoder.encode(req.pw),
            name = req.name,
            adminType = req.adminType
        )
        userRepository.save(user)
        return user
    }
}
