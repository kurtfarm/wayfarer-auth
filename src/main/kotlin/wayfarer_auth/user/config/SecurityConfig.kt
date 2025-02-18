package wayfarer_auth.user.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
//import org.springframework.security.config.annotation.web.builders.HttpSecurity
//import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
//import org.springframework.security.web.SecurityFilterChain
//import org.springframework.web.cors.CorsConfiguration
//import org.springframework.web.cors.CorsConfigurationSource
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource
//import wayfarer_auth.config.jwt.JwtAuthenticationFilter

@Configuration
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
class SecurityConfig(
//    private val jwtAuthenticationFilter: JwtAuthenticationFilter
) {

//    @Bean
//    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
//        return http
//            .csrf { it.disable() }
//            .formLogin { it.disable() }
//            .httpBasic { it.disable() }
//            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
//            //.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
//            .authorizeHttpRequests {
//                it.requestMatchers(
//                    "/api/auth/*"
//                )
//                    .permitAll()
//                    .anyRequest()
//                    .authenticated()
//            }
//            .build()
//    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

//    @Bean
//    fun corsConfigurationSource(): CorsConfigurationSource { //TODO : 게이트웨이 설정에 따라 변경
//        return UrlBasedCorsConfigurationSource().apply {
//            registerCorsConfiguration("/**", CorsConfiguration().apply {
//                allowedOrigins = listOf("*") // 추후에 gateway 주소가 들어갈 예정
//                allowedMethods = listOf("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
//                allowedHeaders = listOf("Authorization", "Authorization-refresh", "Content-Type")
//                allowCredentials = true
//                maxAge = 3600
//            })
//        }
//    }
}
