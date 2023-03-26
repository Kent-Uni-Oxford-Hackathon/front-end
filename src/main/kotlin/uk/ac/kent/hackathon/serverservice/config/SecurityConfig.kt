package uk.ac.kent.hackathon.serverservice.config

import com.vaadin.flow.spring.security.VaadinWebSecurity
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.DelegatingPasswordEncoder
import uk.ac.kent.hackathon.serverservice.routes.LoginRoute

@EnableWebSecurity
@Configuration
class SecurityConfig : VaadinWebSecurity() {

    override fun configure(http: HttpSecurity) {
        super.configure(http)
        setLoginView(http, LoginRoute::class.java)
        http.formLogin().defaultSuccessUrl("/dashboard", true)
    }

    @Bean
    fun passwordEncoder() = DelegatingPasswordEncoder("bcrypt", mapOf("bcrypt" to BCryptPasswordEncoder()))
}
