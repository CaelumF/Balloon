package com.gmail.caelum119.utils.FXUtils

import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.scene.control.TextField

/**
 *
 * Any non-numbers typed into *field* will be removed.;
 */
@Deprecated("")
class TextFieldToNumberField(field: TextField) : ChangeListener<String> {

  internal var field: TextField

  init {
    this.field = field
  }

  override fun changed(observableValue: ObservableValue<out String>, oldText: String, newText: String) {
    //Prevent any non-numbers from being entered by checking if all numbers removed is nothing
    if (newText.replace("[1-9]|0".toRegex(), "") != "") {
      //Staying '0' will cause a stack overflow.
      field.text = if (oldText != "") oldText else "0"
      return
    }
  }

  fun getNumber(): Int {
    return Integer.parseInt(field.text)
  }
}
