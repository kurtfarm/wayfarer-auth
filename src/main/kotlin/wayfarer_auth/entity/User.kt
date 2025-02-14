package wayfarer_auth.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime

@Entity
@Table(name = "user")
@EntityListeners(AuditingEntityListener::class)
class User(
    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,

    @Column(name = "email", nullable = false, unique = true)
    var email: String,

    @Column(name = "password", nullable = false)
    var pw: String,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "phone_number", nullable = false)
    var phoneNumber: String,

    @Column(name = "is_withdrawal", nullable = false)
    var isWithdrawal: Boolean = false // 회원탈퇴 여부 기본값은 false
) : UserDetails {
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    lateinit var createdDate: LocalDateTime

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    lateinit var updatedDate: LocalDateTime

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
