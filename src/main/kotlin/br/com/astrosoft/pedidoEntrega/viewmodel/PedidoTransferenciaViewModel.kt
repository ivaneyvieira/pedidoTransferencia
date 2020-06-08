package br.com.astrosoft.pedidoEntrega.viewmodel

import br.com.astrosoft.AppConfig
import br.com.astrosoft.framework.util.Ssh
import br.com.astrosoft.framework.util.execCommand
import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.framework.viewmodel.fail
import br.com.astrosoft.pedidoEntrega.model.beans.PedidoTransferencia
import java.time.LocalDate

class PedidoTransferenciaViewModel(view: IPedidoTransferenciaView): ViewModel<IPedidoTransferenciaView>(view) {
  fun imprimir() = exec {
    val pedidos =
      view.itensSelecionadoPedido()
        .ifEmpty {fail("Não há pedido selecionado")}
    val impressora = AppConfig.userSaci?.impressora ?: fail("O usuário não possui impresseora")
    pedidos.forEach {pedido ->
      if(printPedido(pedido.lojaOrigem, pedido.numPedido, impressora))
        pedido.marcaImpresso()
    }
    view.showInformation("Impressão finalizada")
    updateGridPedido()
  }
  
  private fun printPedido(storeno: Int, ordno: Int, impressora: String): Boolean {
    return try {
      Ssh("172.20.47.1", "ivaney", "ivaney").shell {
        execCommand("/u/saci/shells/printPedidosTransferencia.sh $storeno $ordno $impressora")
      }
      
      println("/u/saci/shells/printPedidosTransferencia.sh $storeno $ordno $impressora")
      true
    } catch(e: Throwable) {
      false
    }
  }
  
  fun updateGridPedido() {
    view.updateGridPedido(listPedido())
  }
  
  private fun listPedido(): List<PedidoTransferencia> {
    val numPedido = view.numeroPedido
    val data = view.dataPedido
    return PedidoTransferencia.listaPedido()
      .filter {pedido ->
        (pedido.numPedido == numPedido || numPedido == 0) &&
        (pedido.dataPedido == data || data == null)
      }
  }
  
  fun updateGridPedidoMarcado() {
    view.updateGridPedidoMarcado(listPedidoMarcado())
  }
  
  private fun listPedidoMarcado(): List<PedidoTransferencia> {
    val numPedido = view.numeroPedidoMarcado
    val data = view.dataPedidoMarcado
    return PedidoTransferencia.listaPedidoMarcado()
      .filter {pedido ->
        (pedido.numPedido == numPedido || numPedido == 0) &&
        (pedido.dataPedido == data || data == null)
      }
  }
  
  fun updateGridTransferencia() {
    view.updateGridTransferencia(listTransferencia())
  }
  
  private fun listTransferencia(): List<PedidoTransferencia> {
    val numNota = view.numeroTransferencia
    val data = view.dataTransferencia
    return PedidoTransferencia.listaTransferencia()
      .filter {nota ->
        (nota.nfTransferencia.startsWith(numNota) || numNota == "") &&
        (nota.dataPedido == data || data == null)
      }
  }
}

interface IPedidoTransferenciaView: IView {
  fun updateGridPedido(itens: List<PedidoTransferencia>)
  fun itensSelecionadoPedido(): List<PedidoTransferencia>
  
  //
  fun updateGridPedidoMarcado(itens: List<PedidoTransferencia>)
  fun itensSelecionadoPedidoMarcado(): List<PedidoTransferencia>
  
  //
  fun updateGridTransferencia(itens: List<PedidoTransferencia>)
  fun itensSelecionadoTransferencia(): List<PedidoTransferencia>
  
  //
  val numeroPedido: Int
  val dataPedido: LocalDate?
  //
  val numeroPedidoMarcado: Int
  val dataPedidoMarcado: LocalDate?
  
  //
  val numeroTransferencia: String
  val dataTransferencia: LocalDate?
}