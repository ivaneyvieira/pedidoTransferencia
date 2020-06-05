package br.com.astrosoft.pedidoEntrega.model.beans

import br.com.astrosoft.AppConfig
import br.com.astrosoft.pedidoEntrega.model.saci
import java.sql.Time
import java.time.LocalDate
import java.time.LocalTime

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
  val obs: String
                              ) {
  val nfTransferencia: String
    get() = numeroNota(nfnoNota, nfseNota)
  
  private fun numeroNota(nfno: String, nfse: String): String {
    return when {
      nfno == "" -> ""
      nfse == "" -> nfno
      else       -> "$nfno/$nfse"
    }
  }
  
  companion object {
    val storeno
      get() = AppConfig.userSaci?.storeno ?: 0
  
    fun listaPedido(): List<PedidoTransferencia> {
      return saci.listaPedido(storeno)
    }
  
    fun listaTransferencia(): List<PedidoTransferencia> {
      return saci.listaTransferencia(storeno)
    }
  }
}