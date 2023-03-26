package uk.ac.kent.hackathon.serverservice.routes

import com.vaadin.flow.component.login.LoginForm
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.auth.AnonymousAllowed

@Route("login")
@AnonymousAllowed
class LoginRoute : VerticalLayout(), BeforeEnterObserver {

    private val login = LoginForm()

    init {
        addClassName("login-view")
        setSizeFull()
        justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        alignItems = FlexComponent.Alignment.CENTER
        login.action = "login"
        add(login)
    }

    override fun beforeEnter(beforeEnterEvent: BeforeEnterEvent) {
        login.isError = beforeEnterEvent.location.queryParameters.parameters.containsKey("error")
    }
}
