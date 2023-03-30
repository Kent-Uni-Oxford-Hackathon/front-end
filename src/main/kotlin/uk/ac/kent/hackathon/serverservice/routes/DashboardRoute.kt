package uk.ac.kent.hackathon.serverservice.routes

import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.grid.ColumnTextAlign.END
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.*
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.*
import com.vaadin.flow.spring.security.AuthenticationContext
import jakarta.annotation.security.PermitAll
import uk.ac.kent.hackathon.serverservice.domain.Contract
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
    private val contracts: Collection<Contract>,
    private val groups: Collection<String>,
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
    private var chosenGroup = empty<String>()

    private fun grid(
        tokenService: TokenService,
        userDetailsImpl: UserDetailsImpl,
        contracts: Collection<Contract>
    ): Grid<Token> {
        val tokensGrid = Grid(Token::class.java, false)
        tokensGrid.height = "800px"
        tokensGrid.addColumn(Token::tokenId).setHeader("ID")
        tokensGrid.addColumn {
            "This is a ${it.contract.category} token owned by ${it.owner.username}"
        }.setHeader("Description")
        tokensGrid.addComponentColumn {
            Image("/assets/img/${it.contract.imagePath}", "NFT Image")
        }.setHeader("Preview")
        tokensGrid.addComponentColumn {
            Div(Anchor("https://sepolia.etherscan.io/nft/${it.contract.address}/${it.tokenId}", "Details"),
                Button("Send") { Notification.show("This is not implemented yet!") })
        }.setHeader("").textAlign = END
        tokensGrid.setItems(contracts.flatMap { tokenService.getTokensByUserAndCategory(userDetailsImpl, it) })
        return tokensGrid
    }

    override fun setParameter(event: BeforeEvent, @OptionalParameter parameter: String?) {
        chosenCategory = event.location.queryParameters.parameters["category"]
            ?.let { of(it[0]) }
            ?: empty()
        chosenGroup = event.location.queryParameters.parameters["group"]
            ?.let { of(it[0]) }
            ?: empty()
    }

    override fun beforeEnter(event: BeforeEnterEvent?) {
        val groupComboBox = ComboBox<String>("Group")
        groupComboBox.setItems(groups)
        val categoryComboBox = ComboBox<String>("Category")
        chosenGroup.ifPresent { chosenGroup ->
            categoryComboBox.setItems(contracts.filter { chosenGroup == it.group }.map { it.category })
        }

        chosenGroup.ifPresentOrElse({
            groupComboBox.value = it
        }, {
            categoryComboBox.isReadOnly = true
        })
        chosenCategory.ifPresent {
            categoryComboBox.value = it
        }
        groupComboBox.addValueChangeListener { valueChangeEvent ->
            categoryComboBox.setItems(contracts.filter { it.group == valueChangeEvent.value }.map(Contract::category))
            val page = UI.getCurrent().page
            page.fetchCurrentURL {
                val newLocation = "${it.path}?group=${valueChangeEvent.value}"
                page.executeJs("location.href=unescape('$newLocation')")
            }
        }

        categoryComboBox.addValueChangeListener { valueChangeEvent ->
            val page = UI.getCurrent().page
            page.fetchCurrentURL {
                val newLocation = "${it.path}?group=${chosenGroup.get()}&category=${valueChangeEvent.value}"
                page.executeJs("location.href=unescape('$newLocation')")
            }
        }
        add(groupComboBox, categoryComboBox)

        val userDetailsImpl = authenticationContext.getAuthenticatedUser(UserDetailsImpl::class.java).get()
        if (chosenGroup.isEmpty) {
            add(H4("Select a group"))
        } else {
            val contractsToDisplay = if (chosenCategory.isPresent) {
                contracts.filter {
                    it.category == chosenCategory.get()
                }
            } else {
                contracts.filter {
                    it.group == chosenGroup.get()
                }
            }
            add(grid(tokenService, userDetailsImpl, contractsToDisplay))
        }

    }
}