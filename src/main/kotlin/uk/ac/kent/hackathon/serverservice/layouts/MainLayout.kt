package uk.ac.kent.hackathon.serverservice.layouts

import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dependency.StyleSheet
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.spring.security.AuthenticationContext
import uk.ac.kent.hackathon.serverservice.entities.UserDetailsImpl

@StyleSheet("/assets/styles/master.css")
class MainLayout(authenticationContext: AuthenticationContext) : AppLayout() {

    init {
        authenticationContext.getAuthenticatedUser(UserDetailsImpl::class.java).ifPresent {
            val welcomeMessage = Span("Welcome ${it.username}")
            val logoutButton = Button("Logout") { authenticationContext.logout() }

            val navbar = HorizontalLayout(welcomeMessage, logoutButton)
            navbar.setWidthFull()
            navbar.isPadding = true
            navbar.alignItems = CENTER
            navbar.justifyContentMode = FlexComponent.JustifyContentMode.END
            addToNavbar(navbar)
        }

    }
}