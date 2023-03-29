package uk.ac.kent.hackathon.serverservice.routes

import com.vaadin.flow.component.UI
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.grid.ColumnTextAlign.END
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.*
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.*
import com.vaadin.flow.spring.security.AuthenticationContext
import jakarta.annotation.security.PermitAll
import uk.ac.kent.hackathon.serverservice.config.ContractConfig
import uk.ac.kent.hackathon.serverservice.domain.Token
import uk.ac.kent.hackathon.serverservice.entities.UserDetailsImpl
import uk.ac.kent.hackathon.serverservice.layouts.MainLayout
import uk.ac.kent.hackathon.serverservice.services.TokenService
import java.util.Optional.empty
import java.util.Optional.of


@Route("dashboard", layout = MainLayout::class)
@PermitAll
class DashboardRoute(
    private val tokenService: TokenService,
    private val contractConfig: ContractConfig,
    private val authenticationContext: AuthenticationContext,
) : VerticalLayout(), HasUrlParameter<String>, BeforeEnterObserver {
    init {
        val userDetailsImpl = authenticationContext.getAuthenticatedUser(UserDetailsImpl::class.java).get()

        val title = H2("Dashboard")
        val etherscanLink = Anchor(
            "https://sepolia.etherscan.io/address/${userDetailsImpl.etherAccount.ethPkHash}",
            userDetailsImpl.etherAccount.ethPkHash
        )
        val nftHeading = H3("My NFTs")
        val div = Div(title, etherscanLink, nftHeading).apply {
            maxWidth = "800px"
            setWidthFull()
        }
        setHorizontalComponentAlignment(CENTER, div)
        add(div)
    }

    private var chosenCategory = empty<String>()

    private fun grid(
        tokenService: TokenService,
        userDetailsImpl: UserDetailsImpl,
        contractAddress: String,
    ): Grid<Token> {
        val tokensGrid = Grid(Token::class.java, false)
        tokensGrid.addColumn(Token::tokenId).setHeader("ID")
        tokensGrid.addColumn(Token::description).setHeader("Description")
        tokensGrid.addComponentColumn {
            Anchor(
                "https://sepolia.etherscan.io/nft/$contractAddress/${it.tokenId}",
                "Details"
            )
        }.setHeader("").textAlign = END
        tokensGrid.setItems(tokenService.getTokensByUserAndCategory(userDetailsImpl, contractAddress))
        return tokensGrid
    }

    override fun setParameter(event: BeforeEvent, @OptionalParameter parameter: String?) {
        chosenCategory = event.location.queryParameters.parameters["category"]
            ?.let { of(it[0]) }
            ?: empty()
    }

    override fun beforeEnter(event: BeforeEnterEvent?) {
        val comboBox = ComboBox<String>("Category")
        comboBox.setItems(contractConfig.names.asList())
        comboBox.addValueChangeListener { valueChangeEvent ->
            val page = UI.getCurrent().page
            page.fetchCurrentURL {
                val newLocation = "${it.path}?category=${valueChangeEvent.value}"
                page.executeJs("location.href=unescape('$newLocation')")
            }
        }
        add(comboBox)

        val userDetailsImpl = authenticationContext.getAuthenticatedUser(UserDetailsImpl::class.java).get()
        if (chosenCategory.isEmpty) {
            add(H4("Select a category"))
        } else {
            val contractAddress = contractConfig.getCategoriesToAddressPairs().associate { it }[chosenCategory.get()]!!
            add(grid(tokenService, userDetailsImpl, contractAddress))
        }

    }
}