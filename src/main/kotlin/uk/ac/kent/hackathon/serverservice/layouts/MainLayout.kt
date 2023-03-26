package uk.ac.kent.hackathon.serverservice.layouts

import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER
import com.vaadin.flow.component.orderedlayout.FlexLayout
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.spring.security.AuthenticationContext
import uk.ac.kent.hackathon.serverservice.config.ApplicationConfig
import uk.ac.kent.hackathon.serverservice.entities.UserDetailsImpl

class MainLayout(authenticationContext: AuthenticationContext, applicationConfig: ApplicationConfig) : AppLayout() {

    init {
        val logo = H1(applicationConfig.applicationName).apply { addClassName("logo") }
        val horizontalLayout = authenticationContext.getAuthenticatedUser(UserDetailsImpl::class.java).map {
            val welcomeMessage = Span("Welcome ${it.username}")
            val logoutButton = Button("Logout") { authenticationContext.logout() }

            val right = HorizontalLayout(welcomeMessage, logoutButton).apply {
                alignItems = CENTER
                isPadding = true
            }
            val left = HorizontalLayout(logo).apply { isPadding = true }
            FlexLayout(left, right).apply { setSizeFull() }.apply { setFlexGrow(1.0, left) }
        }.orElseGet { FlexLayout(HorizontalLayout(logo).apply { isPadding = true }) }

        addToNavbar(horizontalLayout)
    }
}