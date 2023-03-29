package uk.ac.kent.hackathon.serverservice.routes

import com.vaadin.flow.component.grid.ColumnTextAlign.END
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Anchor
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.html.H3
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route
import com.vaadin.flow.spring.security.AuthenticationContext
import jakarta.annotation.security.PermitAll
import uk.ac.kent.hackathon.serverservice.config.ContractConfig
import uk.ac.kent.hackathon.serverservice.domain.Token
import uk.ac.kent.hackathon.serverservice.entities.UserDetailsImpl
import uk.ac.kent.hackathon.serverservice.layouts.MainLayout
import uk.ac.kent.hackathon.serverservice.services.TokenService

@Route("dashboard", layout = MainLayout::class)
@PermitAll
class DashboardRoute(
    tokenService: TokenService,
    authenticationContext: AuthenticationContext,
    contractConfig: ContractConfig
) : VerticalLayout() {
    init {
        val userDetailsImpl = authenticationContext.getAuthenticatedUser(UserDetailsImpl::class.java).get()

        val title = H2("Dashboard")
        val etherscanLink = Anchor(
            "https://sepolia.etherscan.io/address/${userDetailsImpl.etherAccount.ethPkHash}",
            userDetailsImpl.etherAccount.ethPkHash
        )
        val nftHeading = H3("My NFTs")

        val div = Div(title, etherscanLink, nftHeading, grid(tokenService, userDetailsImpl, contractConfig)).apply {
            maxWidth = "800px"
            setWidthFull()
        }
        setHorizontalComponentAlignment(CENTER, div)
        add(div)
    }

    private fun grid(
        tokenService: TokenService,
        userDetailsImpl: UserDetailsImpl,
        contractConfig: ContractConfig
    ) = Grid(Token::class.java, false)
        .apply { addColumn(Token::tokenId).setHeader("ID") }
        .apply { addColumn(Token::description).setHeader("Description") }
        .apply {
            addComponentColumn {
                Anchor(
                    "https://sepolia.etherscan.io/nft/${contractConfig.address}/${it.tokenId}",
                    "Details"
                )
            }.setHeader("").textAlign = END
        }
        .apply { setItems(tokenService.getTokensByUser(userDetailsImpl)) }
}