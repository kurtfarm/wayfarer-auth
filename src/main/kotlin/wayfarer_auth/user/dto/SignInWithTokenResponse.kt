package wayfarer_auth.user.dto

import wayfarer_auth.jwt.dto.TokenResponse
import wayfarer_auth.user.entity.User

class SignInWithTokenResponse(
    val email: String,
    val tokens: TokenResponse
) {
    companion object {
        fun of(user: User, accessToken: String, refreshToken: String): SignInWithTokenResponse {
            return SignInWithTokenResponse(
                email = user.email,
                tokens = TokenResponse(
                    accessToken = accessToken,
                    refreshToken = refreshToken
                )
            )
        }
    }
}
