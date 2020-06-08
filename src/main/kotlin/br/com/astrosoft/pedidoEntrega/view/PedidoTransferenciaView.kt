package br.com.astrosoft.pedidoEntrega.view

import br.com.astrosoft.framework.view.ViewLayout
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
import br.com.astrosoft.pedidoEntrega.view.PedidoTransferenciaView.Companion.TITLE
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
import com.vaadin.flow.component.icon.VaadinIcon.PRINT
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.provider.ListDataProvider
import com.vaadin.flow.data.value.ValueChangeMode.TIMEOUT
import com.vaadin.flow.function.SerializablePredicate
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import java.time.LocalDate

@Route(layout = PedidoTransferenciaLayout::class)
@PageTitle(TITLE)
@HtmlImport("frontend://styles/shared-styles.html")
class PedidoTransferenciaView: ViewLayout<PedidoTransferenciaViewModel>(), IPedidoTransferenciaView {
  private lateinit var gridPedido: Grid<PedidoTransferencia>
  private lateinit var edtNumeroPedido: TextField
  private lateinit var edtDataPedido: DatePicker
  //
  private lateinit var gridPedidoMarcado: Grid<PedidoTransferencia>
  private lateinit var edtNumeroPedidoMarcado: TextField
  private lateinit var edtDataPedidoMarcado: DatePicker
  //
  private lateinit var gridTransferencia: Grid<PedidoTransferencia>
  private lateinit var edtNumeroTransferencia: TextField
  private lateinit var edtDataTransferencia: DatePicker
  //
  override val viewModel: PedidoTransferenciaViewModel = PedidoTransferenciaViewModel(this)
  private val dataProviderPedido = ListDataProvider<PedidoTransferencia>(mutableListOf())
  private val dataProviderTransferencia = ListDataProvider<PedidoTransferencia>(mutableListOf())
  private val dataProviderPedidoMarcado = ListDataProvider<PedidoTransferencia>(mutableListOf())
  
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
      tab {
        painelPedidoMarcado()
      }.apply {
        val button = Button(TAB_PEDIDO_MARCADO) {
          viewModel.updateGridPedidoMarcado()
        }
        button.addThemeVariants(ButtonVariant.LUMO_SMALL)
        this.addComponentAsFirst(button)
      }
      tab {
        painelTransferencia()
      }.apply {
        val button = Button(TAB_NOTA_FISCAL) {
          viewModel.updateGridTransferencia()
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
          isClearButtonVisible = true
          addValueChangeListener {
            viewModel.updateGridPedido()
          }
        }
      }
      gridPedido = this.grid(dataProvider = dataProviderPedido) {
        isExpand = true
        isMultiSort = true
        addThemeVariants(LUMO_COMPACT)
        setSelectionMode(SelectionMode.MULTI)
        
        addColumnInt(PedidoTransferencia::lojaOrigem) {
          this.setHeader("Lj Origem")
        }
        addColumnInt(PedidoTransferencia::lojaDestino) {
          this.setHeader("Lj Destino")
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
        
        addColumnString(PedidoTransferencia::username) {
          this.setHeader("Usuário")
        }
        addColumnString(PedidoTransferencia::nat) {
          this.setHeader("Nat")
        }
        addColumnString(PedidoTransferencia::doc) {
          this.setHeader("Doc")
        }
        addColumnString(PedidoTransferencia::ent) {
          this.setHeader("Ent")
        }
        addColumnString(PedidoTransferencia::rec) {
          this.setHeader("Rec")
        }
        
        this.shiftSelect()
      }
      
      viewModel.updateGridPedido()
    }
  }
  
  fun HasComponents.painelPedidoMarcado(): VerticalLayout {
    return verticalLayout {
      this.setSizeFull()
      isMargin = false
      isPadding = false
      horizontalLayout {
        setWidthFull()
        button("Desmarcar") {
        
        }
        edtNumeroPedidoMarcado = textField("Numero Pedido") {
          this.valueChangeMode = TIMEOUT
          this.isAutofocus = true
          addValueChangeListener {
            viewModel.updateGridPedidoMarcado()
          }
        }
        edtDataPedidoMarcado = datePicker("Data") {
          localePtBr()
          isClearButtonVisible = true
          addValueChangeListener {
            viewModel.updateGridPedidoMarcado()
          }
        }
      }
      gridPedidoMarcado = this.grid(dataProvider = dataProviderPedidoMarcado) {
        isExpand = true
        isMultiSort = true
        addThemeVariants(LUMO_COMPACT)
        setSelectionMode(SelectionMode.MULTI)
        
        addColumnInt(PedidoTransferencia::lojaOrigem) {
          this.setHeader("Lj Origem")
        }
        addColumnInt(PedidoTransferencia::lojaDestino) {
          this.setHeader("Lj Destino")
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
        
        addColumnString(PedidoTransferencia::username) {
          this.setHeader("Usuário")
        }
        addColumnString(PedidoTransferencia::nat) {
          this.setHeader("Nat")
        }
        addColumnString(PedidoTransferencia::doc) {
          this.setHeader("Doc")
        }
        addColumnString(PedidoTransferencia::ent) {
          this.setHeader("Ent")
        }
        addColumnString(PedidoTransferencia::rec) {
          this.setHeader("Rec")
        }
        
        this.shiftSelect()
      }
      
      viewModel.updateGridPedido()
    }
  }
  
  fun HasComponents.painelTransferencia(): VerticalLayout {
    return verticalLayout {
      this.setSizeFull()
      isMargin = false
      isPadding = false
      horizontalLayout {
        setWidthFull()
        edtNumeroTransferencia = textField("Numero Nota") {
          this.valueChangeMode = TIMEOUT
          this.isAutofocus = true
          addValueChangeListener {
            viewModel.updateGridTransferencia()
          }
        }
        edtDataTransferencia = datePicker("Data") {
          localePtBr()
          isClearButtonVisible = true
          addValueChangeListener {
            viewModel.updateGridTransferencia()
          }
        }
      }
      gridTransferencia = this.grid(dataProvider = dataProviderTransferencia) {
        isExpand = true
        isMultiSort = true
        addThemeVariants(LUMO_COMPACT)
        setSelectionMode(SelectionMode.MULTI)
        
        addColumnInt(PedidoTransferencia::lojaOrigem) {
          this.setHeader("Lj Origem")
        }
        addColumnInt(PedidoTransferencia::lojaDestino) {
          this.setHeader("Lj Destino")
        }
        addColumnString(PedidoTransferencia::nfTransferencia) {
          this.setHeader("Pedido")
        }
        
        addColumnLocalDate(PedidoTransferencia::dataNota) {
          this.setHeader("Data")
        }
        addColumnTime(PedidoTransferencia::horaNota) {
          this.setHeader("Hora")
        }
        
        addColumnString(PedidoTransferencia::obs) {
          this.setHeader("Obs")
        }
        
        this.shiftSelect()
      }
      
      viewModel.updateGridTransferencia()
    }
  }
  
  /*
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
  */
  
  override fun updateGridPedido(itens: List<PedidoTransferencia>) {
    gridPedido.deselectAll()
    dataProviderPedido.updateItens(itens)
  }
  
  override fun updateGridPedidoMarcado(itens: List<PedidoTransferencia>) {
    gridPedidoMarcado.deselectAll()
    dataProviderPedidoMarcado.updateItens(itens)
  }
  
  override fun updateGridTransferencia(itens: List<PedidoTransferencia>) {
    gridTransferencia.deselectAll()
    dataProviderTransferencia.updateItens(itens)
  }
  
  override fun itensSelecionadoPedido(): List<PedidoTransferencia> {
    return gridPedido.selectedItems.toList()
  }
  
  override fun itensSelecionadoPedidoMarcado(): List<PedidoTransferencia> {
    return gridPedidoMarcado.selectedItems.toList()
  }
  
  override fun itensSelecionadoTransferencia(): List<PedidoTransferencia> {
    return gridTransferencia.selectedItems.toList()
  }
  
  override val numeroPedido: Int
    get() = edtNumeroPedido.value?.toIntOrNull() ?: 0
  override val dataPedido: LocalDate?
    get() = edtDataPedido.value
  override val numeroPedidoMarcado: Int
    get() = edtNumeroPedidoMarcado.value?.toIntOrNull() ?: 0
  override val dataPedidoMarcado: LocalDate?
    get() = edtDataPedidoMarcado.value
  override val numeroTransferencia: String
    get() = edtNumeroTransferencia.value ?: ""
  override val dataTransferencia: LocalDate?
    get() = edtDataTransferencia.value
  
  companion object {
    const val TAB_PEDIDO: String = "Pedido"
    const val TAB_PEDIDO_MARCADO: String = "Pedido Impresso"
    const val TAB_NOTA_FISCAL: String = "Nota Fiscal"
    const val TITLE = "Pedidos"
  }
}
