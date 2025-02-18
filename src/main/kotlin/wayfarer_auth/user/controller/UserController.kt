package wayfarer_auth.user.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import wayfarer_auth.jwt.dto.TokenResponse
import wayfarer_auth.jwt.service.TokenService
import wayfarer_auth.user.dto.SignUpResponse
import wayfarer_auth.user.dto.SignUpWithTokenResponse
import wayfarer_auth.user.service.UserService

@RestController
@RequestMapping("/api/auth")
class UserController (
    private val userService: UserService,
    private val tokenService: TokenService
){
    @PostMapping("/signup")
    fun signUp(@RequestBody req: SignUpResponse): ResponseEntity<SignUpWithTokenResponse> {
        val user = userService.register(req)
        val accessToken = tokenService.generateAccessToken(user.id)
        val refreshToken = tokenService.generateRefreshToken(user.id)

        val response = SignUpWithTokenResponse(
            user = SignUpResponse(
                email = user.email,
                pw = user.getPassword(),
                name = user.name,
                adminType = user.adminType
            ),
            tokens = TokenResponse(
                accessToken = accessToken,
                refreshToken = refreshToken
            )
        )

        return ResponseEntity.ok(response)
    }
}
