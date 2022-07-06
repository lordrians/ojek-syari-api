package com.lordrians.ojeksyariapi.authentication

import com.fasterxml.jackson.databind.ObjectMapper
import com.lordrians.ojeksyariapi.utils.BaseResponse
import com.lordrians.ojeksyariapi.utils.Constant
import com.lordrians.ojeksyariapi.utils.Empty
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.stream.Collectors
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthenticationFilter: OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            if (JwtConfig.isPermit(request)){
                filterChain.doFilter(request,response)
            } else {
                val claims = validate(request)
                if (claims[Constant.CLAIMS] != null){
                    setupAuthentication(claims)
                } else {
                    SecurityContextHolder.clearContext()
                    throw Exception("Token required")
                }
            }
        } catch (e: Exception){
            val errorResponse = BaseResponse<Empty>(
                status = false,
                message = e.localizedMessage,
                data = null
            )
            response.contentType = "application/json"
            val responseString = ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(errorResponse)
            response.writer.println(responseString)
        }
    }

    private fun validate(request: HttpServletRequest): Claims {
        val jwtToken = request.getHeader("Authentication")
        return Jwts.parserBuilder()
            .setSigningKey(Constant.SECRET.toByteArray())
            .build()
            .parseClaimsJws(jwtToken)
            .body
    }

    private fun setupAuthentication(claims: Claims){
        val authorities = claims[Constant.CLAIMS] as List<String>
        val authStream = authorities.stream().map {SimpleGrantedAuthority(it)}
            .collect(Collectors.toList())

        val auth = UsernamePasswordAuthenticationToken(claims.subject, null, authStream)
        SecurityContextHolder.getContext().authentication = auth
    }
}