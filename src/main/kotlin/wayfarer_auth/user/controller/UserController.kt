package wayfarer_auth.user.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import wayfarer_auth.jwt.service.TokenService
import wayfarer_auth.user.dto.SignInRequest
import wayfarer_auth.user.dto.SignInWithTokenResponse
import wayfarer_auth.user.dto.SignUpRequest
import wayfarer_auth.user.service.UserService

@RestController
@RequestMapping("/api/auth")
class UserController(
    private val userService: UserService,
    private val tokenService: TokenService
) {
    @PostMapping("/signup")
    fun signUp(@RequestBody req: SignUpRequest): ResponseEntity<String> {
        userService.register(req)
        return ResponseEntity.ok("회원가입 성공")
    }

    @PostMapping("/login")
    fun login(@RequestBody req: SignInRequest): ResponseEntity<SignInWithTokenResponse> {
        val response = userService.signIn(req) ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/logout")
    fun logout(@RequestHeader("Refresh-Token") refreshToken: String): ResponseEntity<String> {
        userService.signOut(refreshToken)
        return ResponseEntity.ok("로그아웃 성공")
    }
}
