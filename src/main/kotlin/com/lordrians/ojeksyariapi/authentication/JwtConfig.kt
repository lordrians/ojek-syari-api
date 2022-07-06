package com.lordrians.ojeksyariapi.authentication

import com.lordrians.ojeksyariapi.utils.*
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.util.*
import java.util.stream.Collectors
import javax.servlet.http.HttpServletRequest


@EnableWebSecurity
@Configuration
class JwtConfig : WebSecurityConfigurerAdapter(){

    @Autowired
    private lateinit var authenticationFilter: AuthenticationFilter

    override fun configure(http: HttpSecurity) {
        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .csrf().disable()
            .addFilterAt(authenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .authorizeRequests()
            .antMatchers(HttpMethod.POST, *postPermit.toTypedArray()).permitAll()
            .antMatchers(HttpMethod.GET, *getPermit.toTypedArray()).permitAll()
            .anyRequest().authenticated()
    }
    companion object {
        val postPermit = listOf(
            "/api/user/login",
            "/api/user/register",
            "/api/customer/register",
            "/api/customer/login",
            "/api/driver/login",
            "/api/driver/register"
        )
        val getPermit = listOf(
            "/api/ping"
        )

        fun generateToken(id: String?, username: String?): String {
            val expired = Date(System.currentTimeMillis() + (60_000 * 60 * 24))
            val granted = AuthorityUtils.commaSeparatedStringToAuthorityList(username)
            val grantedStream = granted.stream().map { it.authority }.collect(Collectors.toList())
            return Jwts.builder()
                .setSubject(id)
                .claim(Constant.CLAIMS,grantedStream)
                .setExpiration(expired)
                .signWith(Keys.hmacShaKeyFor(Constant.SECRET.toByteArray()), SignatureAlgorithm.HS256)
                .compact()
        }

        fun isPermit(request: HttpServletRequest) : Boolean {
            val path = request.servletPath
            return postPermit.contains(path) or getPermit.contains(path)
        }
    }
}