package helpers

import views.html.customElements.plainInput
import views.html.helper.FieldConstructor

object PlainInputHelper {
  implicit val myFields = FieldConstructor(plainInput.f)
}
