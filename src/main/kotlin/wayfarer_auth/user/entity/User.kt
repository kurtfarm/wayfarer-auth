package wayfarer_auth.user.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import wayfarer_auth.user.dto.SignUpRequest

@Entity
@Table(name = "user")
@EntityListeners(AuditingEntityListener::class)
class User(
    @Column(name = "email", nullable = false, unique = true)
    var email: String,

    @Column(name = "password", nullable = false)
    var pw: String,

    @Column(name = "name", nullable = false)
    var name: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "admin_type", nullable = false)
    var adminType: AdminType,

//    @Column(name = "phone_number", nullable = false)
//    var phoneNumber: String,

) : BaseEntity(), UserDetails {
    companion object {
        fun of(request: SignUpRequest, passwordEncoder: PasswordEncoder): User {
            return User(
                email = request.email,
                pw = passwordEncoder.encode(request.pw),
                name = request.name,
                adminType = request.adminType
            )
        }
    }

    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L

    @Column(name = "is_withdrawal", nullable = false)
    var isWithdrawal: Boolean = false // 회원탈퇴 여부 기본값은 false

    // UserDetails 인터페이스 메서드 구현
    override fun getAuthorities() =
        listOf(SimpleGrantedAuthority("ROLE_USER")) // TODO : 별도의 role 필드 필요? => 화면설계서 보고 정의

    override fun getPassword(): String = pw

    override fun getUsername(): String = email

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true // 패스워드 만료 여부

    override fun isEnabled(): Boolean = !isWithdrawal
}
