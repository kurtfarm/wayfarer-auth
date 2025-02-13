//package wayfarer_auth.config.jwt
//
//import jakarta.servlet.FilterChain
//import jakarta.servlet.http.HttpServletRequest
//import jakarta.servlet.http.HttpServletResponse
//import org.springframework.security.core.context.SecurityContextHolder
//import org.springframework.stereotype.Component
//import org.springframework.web.filter.OncePerRequestFilter
//
//@Component
//class JwtAuthenticationFilter(
//    private val jwtTokenProvider: TokenProvider
//) : OncePerRequestFilter() {
//
//    companion object {
//        private const val AUTHORIZATION_HEADER = "Authorization"
//        private const val TOKEN_PREFIX = "Bearer "
//    }
//
//    override fun doFilterInternal(
//        request: HttpServletRequest,
//        response: HttpServletResponse,
//        filterChain: FilterChain
//    ) {
//        try {
//            val token = resolveToken(request as HttpServletRequest)
//
//            token?.let {
//                if (jwtTokenProvider.validateAccessToken(it)) {
//                    val authentication = jwtTokenProvider.getAuthentication(it)
//                    SecurityContextHolder.getContext().authentication = authentication
//                }
//            }
//        } catch (e: Exception) {
//            request?.setAttribute("exception", e)
//        }
//
//        filterChain?.doFilter(request, response)
//    }
//
//    private fun resolveToken(request: HttpServletRequest): String? =
//        request.getHeader(AUTHORIZATION_HEADER)
//            ?.takeIf { it.startsWith(TOKEN_PREFIX) }
//            ?.substring(TOKEN_PREFIX.length)
//
//}
