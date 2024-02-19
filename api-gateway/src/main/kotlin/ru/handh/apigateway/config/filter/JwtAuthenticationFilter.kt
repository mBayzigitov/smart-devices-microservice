package ru.handh.apigateway.config.filter

import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerExceptionResolver
import ru.handh.apigateway.service.JwtService
import ru.handh.apigateway.service.RequestContext
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val handlerExceptionResolver: HandlerExceptionResolver
) : OncePerRequestFilter() {

    companion object {
        val excludedUris = setOf(
            "/api/refresh"
        )
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return excludedUris.contains(request.requestURI)
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        try {
            extractToken(request)?.let {
                val userId = jwtService.parseTokenInfo(it).userId
                RequestContext.set(userId)
            }

            filterChain.doFilter(request, response)
        } catch (exc: Exception) {
            exc.printStackTrace()
            handlerExceptionResolver.resolveException(request, response, null, exc)
        }

    }

    private fun extractToken(request: HttpServletRequest): String? {
        return request.getHeader("X-Access-Token")
    }
}
