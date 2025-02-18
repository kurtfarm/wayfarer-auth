package wayfarer_auth.user.dto

import wayfarer_auth.user.entity.AdminType

data class SignUpRequest(
    val email: String,
    val pw: String,
    val name: String,
    val adminType: AdminType
)
