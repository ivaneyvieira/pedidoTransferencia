package br.com.astrosoft.pedidoTransferencia.model.beans

import br.com.astrosoft.AppConfig
import br.com.astrosoft.pedidoTransferencia.model.saci
import java.sql.Time
import java.time.LocalDate

data class PedidoTransferencia(
  val lojaOrigem: Int,
  val lojaDestino: Int,
  val numPedido: Int,
  val dataPedido: LocalDate?,
  val horaPedido: Time?,
  val metodo: Int,
  val nfnoNota: String,
  val nfseNota: String,
  val dataNota: LocalDate?,
  val horaNota: Time,
  val obs: String,
  val username: String,
  val marca: String,
  val nat: String,
  val doc: String,
  val ent: String,
  val rec: String,
  val tipo: String) {
  val nfTransferencia: String
    get() = numeroNota(nfnoNota, nfseNota)
  
  private fun numeroNota(nfno: String, nfse: String): String {
    return when {
      nfno == "" -> ""
      nfse == "" -> nfno
      else       -> "$nfno/$nfse"
    }
  }
  
  fun marcaImpresso() {
    saci.ativaMarca(lojaOrigem, numPedido, "S")
  }
  
  fun desmarcaImpresso() {
    saci.ativaMarca(lojaOrigem, numPedido, " ")
  }
  
  companion object {
    val storeno
      get() = AppConfig.userSaci?.storeno ?: 0
    
    fun listaPedido(): List<PedidoTransferencia> {
      return saci.listaPedido(storeno)
        .filter {it.marca != "S"}
    }
    
    fun listaPedidoMarcado(): List<PedidoTransferencia> {
      return saci.listaPedido(storeno)
        .filter {it.marca == "S"}
    }
    
    fun listaTransferencia(): List<PedidoTransferencia> {
      return saci.listaTransferencia(storeno)
    }
  }
}