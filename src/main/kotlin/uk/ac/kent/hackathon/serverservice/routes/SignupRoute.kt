package uk.ac.kent.hackathon.serverservice.routes

import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant.LUMO_PRIMARY
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.H3
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.notification.Notification.show
import com.vaadin.flow.component.notification.NotificationVariant.LUMO_SUCCESS
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.PasswordField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.auth.AnonymousAllowed
import org.springframework.security.core.userdetails.UserDetails
import uk.ac.kent.hackathon.serverservice.entities.UserDetailsImpl
import uk.ac.kent.hackathon.serverservice.layouts.MainLayout
import uk.ac.kent.hackathon.serverservice.services.UserDetailsService


@Route("signup", layout = MainLayout::class)
@AnonymousAllowed
class SignupRoute(userDetailsService: UserDetailsService) : VerticalLayout() {
    init {
        val title = H3("Signup")
        val username = TextField("Username").apply {
            isRequiredIndicatorVisible = true
            setWidthFull()
        }
        val password = PasswordField("Password").apply {
            isRequiredIndicatorVisible = true
            setWidthFull()
        }
        val passwordConfirm =
            PasswordField("Confirm password").apply {
                isRequiredIndicatorVisible = true
                setWidthFull()
            }
        val errorMessageField = Span()
        val submitButton = Button("Join the community") {
            val user = UserDetailsImpl(username.value, password.value)
            userDetailsService.createUser(user)
            showSuccess(user)
        }.apply {
            setWidthFull()
            addThemeVariants(LUMO_PRIMARY)
        }

        val div = Div(title, username, password, passwordConfirm, errorMessageField, submitButton).apply {
            maxWidth = "500px"
        }
        setHorizontalComponentAlignment(Alignment.CENTER, div)
        justifyContentMode = JustifyContentMode.CENTER
        add(div)
        setSizeFull()
    }

    private fun showSuccess(userDetails: UserDetails) {
        val notification = show("Data saved, welcome " + userDetails.username)
        notification.addThemeVariants(LUMO_SUCCESS)
        UI.getCurrent().navigate(DashboardRoute::class.java)
    }

}