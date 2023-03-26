package uk.ac.kent.hackathon.serverservice.routes

import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route
import com.vaadin.flow.spring.security.AuthenticationContext
import jakarta.annotation.security.PermitAll
import org.springframework.security.core.userdetails.UserDetails

@Route("dashboard")
@PermitAll
class DashboardRoute(authenticationContext: AuthenticationContext) : VerticalLayout() {
    init {
        add(authenticationContext.getAuthenticatedUser(UserDetails::class.java).get().username)
    }
}