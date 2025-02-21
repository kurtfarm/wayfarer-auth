package wayfarer_auth.user.service

import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import wayfarer_auth.jwt.service.TokenService
import wayfarer_auth.user.dto.SignInRequest
import wayfarer_auth.user.dto.SignInWithTokenResponse
import wayfarer_auth.user.dto.SignUpRequest
import wayfarer_auth.user.entity.User
import wayfarer_auth.user.repository.UserRepository

@Service
class UserService(
    private val userRepository: UserRepository,
    private val tokenService: TokenService,
    private val passwordEncoder: PasswordEncoder
) {
    @Transactional
    fun register(req: SignUpRequest) {
        val user = User.of(req, passwordEncoder)
        userRepository.save(user)
    }

    @Transactional
    fun signIn(req: SignInRequest): SignInWithTokenResponse? {
        val user = userRepository.findByEmail(req.email) ?: return null

        return if (passwordEncoder.matches(req.pw, user.getPassword())) {
            val accessToken = tokenService.generateAccessToken(user.id)
            val refreshToken = tokenService.generateRefreshToken(user.id)

            SignInWithTokenResponse.of(user, accessToken, refreshToken)
        } else {
            null
        }
    }

    @Transactional
    fun signOut(refreshToken: String) {
        tokenService.deleteRefreshToken(refreshToken)
    }
}
