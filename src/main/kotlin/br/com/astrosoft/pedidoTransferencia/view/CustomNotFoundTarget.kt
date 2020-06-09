package br.com.astrosoft.pedidoTransferencia.view

import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.ErrorParameter
import com.vaadin.flow.router.NotFoundException
import com.vaadin.flow.router.ParentLayout
import com.vaadin.flow.router.RouteNotFoundError
import javax.servlet.http.HttpServletResponse

@ParentLayout(PedidoTransferenciaLayout::class)
class CustomNotFoundTarget: RouteNotFoundError() {
  override fun setErrorParameter(event: BeforeEnterEvent,
                                 parameter: ErrorParameter<NotFoundException>): Int {
    element.text = "My custom not found class!"
    return HttpServletResponse.SC_NOT_FOUND
  }
}