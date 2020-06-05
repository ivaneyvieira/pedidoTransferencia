package br.com.astrosoft.pedidoEntrega.view

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.view.ViewLayout
import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import br.com.astrosoft.framework.view.addColumnTime
import br.com.astrosoft.framework.view.list
import br.com.astrosoft.framework.view.localePtBr
import br.com.astrosoft.framework.view.shiftSelect
import br.com.astrosoft.framework.view.updateItens
import br.com.astrosoft.pedidoEntrega.model.beans.PedidoTransferencia
import br.com.astrosoft.pedidoEntrega.model.beans.UserSaci
import br.com.astrosoft.pedidoEntrega.viewmodel.IPedidoTransferenciaView
import br.com.astrosoft.pedidoEntrega.viewmodel.PedidoTransferenciaViewModel
import com.github.mvysny.karibudsl.v10.VaadinDsl
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.datePicker
import com.github.mvysny.karibudsl.v10.grid
import com.github.mvysny.karibudsl.v10.horizontalLayout
import com.github.mvysny.karibudsl.v10.isExpand
import com.github.mvysny.karibudsl.v10.tabSheet
import com.github.mvysny.karibudsl.v10.textField
import com.github.mvysny.karibudsl.v10.verticalLayout
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.dependency.HtmlImport
import com.vaadin.flow.component.grid.ColumnTextAlign.END
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.Grid.SelectionMode
import com.vaadin.flow.component.grid.GridVariant.LUMO_COMPACT
import com.vaadin.flow.component.icon.VaadinIcon.CLOSE
import com.vaadin.flow.component.icon.VaadinIcon.PRINT
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.provider.ListDataProvider
import com.vaadin.flow.data.value.ValueChangeMode.TIMEOUT
import com.vaadin.flow.function.SerializablePredicate
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import java.time.LocalDate

@Route(layout = PedidoEntregaLayout::class)
@PageTitle("Pedidos")
@HtmlImport("frontend://styles/shared-styles.html")
class PedidoEntregaView: ViewLayout<PedidoTransferenciaViewModel>(), IPedidoTransferenciaView {
  private lateinit var gridPedidos: Grid<PedidoTransferencia>
  private lateinit var edtNumeroPedido: TextField
  private lateinit var edtDataPedido: DatePicker

  override val viewModel: PedidoTransferenciaViewModel = PedidoTransferenciaViewModel(this)
  private val dataProviderPedidoImprimir = ListDataProvider<PedidoTransferencia>(mutableListOf())
  
  override fun isAccept(user: UserSaci) = true
  
  init {
    tabSheet {
      setSizeFull()
      tab {
        painelPedido()
      }.apply {
        val button = Button(TAB_PEDIDO) {
          viewModel.updateGridPedido()
        }
        button.addThemeVariants(ButtonVariant.LUMO_SMALL)
        this.addComponentAsFirst(button)
      }
    }
  }
  
  fun HasComponents.painelPedido(): VerticalLayout {
    return verticalLayout {
      this.setSizeFull()
      isMargin = false
      isPadding = false
      horizontalLayout {
        setWidthFull()
        button("Imprimir") {
          icon = PRINT.create()
          addClickListener {
            viewModel.imprimir()
          }
        }
        edtNumeroPedido = textField("Numero Pedido") {
          this.valueChangeMode = TIMEOUT
          this.isAutofocus = true
          addValueChangeListener {
            viewModel.updateGridPedido()
          }
        }
        edtDataPedido = datePicker("Data") {
          localePtBr()
          setClearButtonVisible(true)
          addValueChangeListener {
            viewModel.updateGridPedido()
          }
        }
      }
      gridPedidos = this.grid(dataProvider = dataProviderPedidoImprimir) {
        isExpand = true
        isMultiSort = true
        addThemeVariants(LUMO_COMPACT)
        setSelectionMode(SelectionMode.MULTI)
        
        addColumnSeq("Num")
        addColumnInt(PedidoTransferencia::lojaOrigem) {
          this.setHeader("Lj Origem")
        }
        addColumnInt(PedidoTransferencia::lojaDestino) {
          this.setHeader("Lj Origem")
        }
        addColumnInt(PedidoTransferencia::numPedido) {
          this.setHeader("Pedido")
        }
        
        addColumnLocalDate(PedidoTransferencia::dataPedido) {
          this.setHeader("Data")
        }
        addColumnTime(PedidoTransferencia::horaPedido) {
          this.setHeader("Hora")
        }

        addColumnString(PedidoTransferencia::obs) {
          this.setHeader("Obs")
        }

        this.shiftSelect()
      }
      
      viewModel.updateGridPedido()
    }
  }
  
  private fun @VaadinDsl Grid<PedidoTransferencia>.addColumnSeq(label: String) {
    addColumn {
      val lista = list(this)
      lista.indexOf(it) + 1
    }.apply {
      this.textAlign = END
      isAutoWidth = true
      setHeader(label)
    }
  }
  
  override fun updateGridPedido(itens: List<PedidoTransferencia>) {
    gridPedidos.deselectAll()
    dataProviderPedidoImprimir.updateItens(itens)
  }
  
  
  override fun itensSelecionadoPedido(): List<PedidoTransferencia> {
    return gridPedidos.selectedItems.toList()
  }
  

  override val numeroPedido: Int
    get() = edtNumeroPedido.value?.toIntOrNull() ?: 0
  override val dataPedido: LocalDate?
    get() = edtDataPedido.value

  
  companion object {
    const val TAB_PEDIDO: String = "Pedido"
    const val TAB_NOTA_FISCAL: String = "Nota Fiscal"
    const val TAB_EM_TRANSITO: String = "Em Transito"
  }
}

class PedicateTrue<T>: SerializablePredicate<T> {
  override fun test(p0: T?): Boolean = true
}
