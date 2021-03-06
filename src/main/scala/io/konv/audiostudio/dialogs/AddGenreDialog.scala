package io.konv.audiostudio.dialogs

import javafx.scene.Parent
import javafx.stage.Stage

import io.konv.audiostudio.Main
import io.konv.audiostudio.controllers.AddGenreForm
import io.konv.audiostudio.models.Genre

import scalafx.Includes._
import scalafx.scene.control.{ButtonType, Dialog}
import scalafx.scene.image.Image
import scalafxml.core.FXMLLoader

class AddGenreDialog extends Dialog[Genre] {

  val loader = new FXMLLoader(Main.getClass.getResource("/fxml/dialog_add_genre.fxml"), null)

  title = "Audio Studio Manager"
  headerText = "Add Genre"

  dialogPane().content = loader.load[Parent]
  dialogPane().buttonTypes = Seq(ButtonType.OK, ButtonType.Cancel)
  dialogPane().getScene.getWindow.asInstanceOf[Stage].icons += new Image("img/icon.png")

  resultConverter = buttonType => {
    if (buttonType == ButtonType.OK) loader.getController[AddGenreForm].get()
    else null
  }
}

