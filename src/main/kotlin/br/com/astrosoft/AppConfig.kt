package br.com.astrosoft

import br.com.astrosoft.framework.spring.SecurityUtils
import br.com.astrosoft.framework.view.ViewUtil

object AppConfig {
  val version = ViewUtil.versao
  const val commpany = "Engecopi"
  const val title = "Pedido Transferencia"
  const val shortName = "Transferencia"
  const val iconPath = "icons/logo.png"
  
  val userDetails get() = SecurityUtils.userDetails
  val userSaci get() = userDetails?.userSaci
}
