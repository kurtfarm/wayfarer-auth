package wayfarer_auth.user.dto

data class SignInRequest(
    val email: String,
    val pw: String
)
