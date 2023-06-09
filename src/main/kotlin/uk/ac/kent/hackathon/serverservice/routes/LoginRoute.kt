package uk.ac.kent.hackathon.serverservice.routes

import com.vaadin.flow.component.dependency.StyleSheet
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.login.LoginForm
import com.vaadin.flow.component.login.LoginOverlay
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.auth.AnonymousAllowed
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo
import com.vaadin.flow.theme.material.Material
import uk.ac.kent.hackathon.serverservice.config.ApplicationConfig
import uk.ac.kent.hackathon.serverservice.layouts.MainLayout

@Route("login", layout = MainLayout::class)
@AnonymousAllowed
class LoginRoute(applicationConfig: ApplicationConfig) : VerticalLayout(), BeforeEnterObserver {

    private val loginOverlay = LoginForm()

    init {
        addClassName("login-view")
        setSizeFull()
        justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        alignItems = FlexComponent.Alignment.CENTER
        loginOverlay.action = "login"
        loginOverlay.addClassName("login-modal")
        loginOverlay.isForgotPasswordButtonVisible = false
        add(loginOverlay)
    }

    override fun beforeEnter(beforeEnterEvent: BeforeEnterEvent) {
        loginOverlay.isError = beforeEnterEvent.location.queryParameters.parameters.containsKey("error")
    }
}
