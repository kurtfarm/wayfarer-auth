package wayfarer_auth.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import wayfarer_auth.entity.User

@Repository
interface UserRepository : JpaRepository<User, Long> {
}
