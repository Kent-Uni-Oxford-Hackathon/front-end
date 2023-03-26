package uk.ac.kent.hackathon.serverservice.routes

import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route
import jakarta.annotation.security.PermitAll
import uk.ac.kent.hackathon.serverservice.layouts.MainLayout

@Route("dashboard", layout = MainLayout::class)
@PermitAll
class DashboardRoute : VerticalLayout() {
    init {
        add(H1("Dashboard"))
    }
}