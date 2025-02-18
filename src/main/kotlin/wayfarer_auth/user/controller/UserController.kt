package wayfarer_auth.user.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import wayfarer_auth.jwt.dto.TokenResponse
import wayfarer_auth.jwt.service.TokenService
import wayfarer_auth.user.dto.SignInRequest
import wayfarer_auth.user.dto.SignInWithTokenResponse
import wayfarer_auth.user.dto.SignUpRequest
import wayfarer_auth.user.service.UserService

@RestController
@RequestMapping("/api/auth")
class UserController (
    private val userService: UserService,
    private val tokenService: TokenService
){
    @PostMapping("/signup")
    fun signUp(@RequestBody req: SignUpRequest):ResponseEntity<String> {
        val user = userService.register(req)
        return ResponseEntity.ok("회원가입 성공")
    }

    @PostMapping("/login")
    fun login(@RequestBody req: SignInRequest): ResponseEntity<SignInWithTokenResponse> {
        val user = userService.signIn(req) ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null)

        val accessToken = tokenService.generateAccessToken(user.id)
        val refreshToken = tokenService.generateRefreshToken(user.id)

        val response = SignInWithTokenResponse(
            email = user.email,
            tokens = TokenResponse(
                accessToken = accessToken,
                refreshToken = refreshToken
            )
        )

        return ResponseEntity.ok(response)
    }

}
