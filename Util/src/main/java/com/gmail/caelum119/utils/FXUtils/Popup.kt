package com.gmail.caelum119.utils.FXUtils

import javafx.scene.Scene
import javafx.scene.layout.Pane
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.StageStyle

/**
 * First created 4/28/2016 in Engine
 */
public class Popup(val owner: Stage): Pane(){
  val dialog = Stage()

  init {
    dialog.initModality(Modality.NONE)
    dialog.initStyle(StageStyle.UNDECORATED)
    dialog.initOwner(owner)
    dialog.scene = Scene(this)
  }

  fun show(){
    dialog.show()
  }
}