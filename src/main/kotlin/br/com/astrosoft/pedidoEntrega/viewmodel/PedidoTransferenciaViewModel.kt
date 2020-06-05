package br.com.astrosoft.pedidoEntrega.viewmodel

import br.com.astrosoft.framework.util.Ssh
import br.com.astrosoft.framework.util.execCommand
import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.pedidoEntrega.model.beans.PedidoTransferencia
import java.time.LocalDate

class PedidoTransferenciaViewModel(view: IPedidoTransferenciaView): ViewModel<IPedidoTransferenciaView>(view) {
  fun imprimir() = exec {
  }
  
  private fun printPedido(storeno: Int, ordno: Int, impressora: String): Boolean {
    return try {
      Ssh("172.20.47.1", "ivaney", "ivaney").shell {
        execCommand("/u/saci/shells/printPedidos.sh $storeno $ordno $impressora")
      }
      
      println("/u/saci/shells/printPedidos.sh $storeno $ordno $impressora")
      true
    } catch(e: Throwable) {
      false
    }
  }
  
  fun updateGridPedido() {
    view.updateGridPedido(listPedidoTransferencia())
  }
  
  private fun listPedidoTransferencia(): List<PedidoTransferencia> {
    val numPedido = view.numeroPedido
    val data = view.dataPedido
    return PedidoTransferencia.listaPedido()
      .filter {pedido ->
        (pedido.numPedido == numPedido || numPedido == 0) &&
        (pedido.dataPedido == data || data == null)
      }
  }
}

interface IPedidoTransferenciaView: IView {
  fun updateGridPedido(itens: List<PedidoTransferencia>)
  
  fun itensSelecionadoPedido(): List<PedidoTransferencia>
  
  //
  val numeroPedido: Int
  val dataPedido: LocalDate?
}