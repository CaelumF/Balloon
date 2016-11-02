package com.gmail.caelum119.utils.FXUtils

import javafx.scene.Node
import javafx.scene.layout.AnchorPane

/**

 */
object  FXUtils {
  /**
   *
   */
  public fun anchor(top: Double? = null, bottom: Double? = null, right: Double? = null, left: Double? = null, node: Node ) {
    top?.let { AnchorPane.setTopAnchor(node, top)}
    bottom.let { AnchorPane.setBottomAnchor(node, bottom) }
    right?.let { AnchorPane.setRightAnchor(node, right) }
    left?.let { AnchorPane.setLeftAnchor(node, right) }
  }
}
