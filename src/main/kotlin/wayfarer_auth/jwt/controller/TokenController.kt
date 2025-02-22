package wayfarer_auth.jwt.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import wayfarer_auth.jwt.dto.TokenResponse
import wayfarer_auth.jwt.service.TokenService

@RestController
@RequestMapping("/api/token")
class TokenController (
    private val tokenService: TokenService
){
    @PostMapping("/reissue")
    fun reissue(
        @RequestHeader("Refresh-Token") refreshToken: String
    ): ResponseEntity<TokenResponse> {
        val response = tokenService.reissueRefreshToken(refreshToken)
        return ResponseEntity.ok(response)
    }
}
