package io.konv.audiostudio.controllers

import java.sql.Date
import javafx.collections.FXCollections
import javafx.scene.input.KeyCode

import io.konv.audiostudio.{Alerts, DBManager}
import io.konv.audiostudio.Includes._
import slick.jdbc.GetResult

import scala.util.{Failure, Success}
import scalafx.scene.control.{ButtonType, TableColumn, TableView}
import scalafxml.core.macros.sfxml
import slick.jdbc.PostgresProfile.api._

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext.Implicits.global

trait AlbumsTabTrait {
  def update(): Unit
}

case class AlbumsTabItem(id: Int, title: String, price: Int, date: Date, songsCount: Int)

@sfxml
class AlbumsTabController(table: TableView[AlbumsTabItem],
                          titleColumn: TableColumn[AlbumsTabItem, String],
                          priceColumn: TableColumn[AlbumsTabItem, String],
                          dateColumn: TableColumn[AlbumsTabItem, String],
                          songsCountColumn: TableColumn[AlbumsTabItem, String]) extends AlbumsTabTrait {

  titleColumn.cellValueFactory = v => v.value.title
  priceColumn.cellValueFactory = v => v.value.price.toString
  dateColumn.cellValueFactory = v => v.value.date.toString
  songsCountColumn.cellValueFactory = v => v.value.songsCount.toString

  update()

  table.onKeyPressed = k => k.getCode match {
    case KeyCode.DELETE => delete()
    case KeyCode.F5 => update()
    case _ => ()
  }


  override def update(): Unit = {
    implicit val getResult = GetResult[AlbumsTabItem](r => AlbumsTabItem(r.<<, r.<<, r.<<, r.<<, r.<<))
    val query = sql"SELECT album.id, album.title, album.price,album.released_date, 0 FROM album".as[AlbumsTabItem]
    DBManager.db.run(query).onComplete {
      case Success(v) => table.items.set(FXCollections.observableList(v.asJava))
      case Failure(v) => ()
    }
  }

  private def delete(): Unit = {
    if (table.getSelectionModel.isEmpty) return
    val album = table.getSelectionModel.getSelectedItem
    Alerts.confirm("Delete Album", s"Do you really want to remove artist ${album.title}?") match {
      case Some(ButtonType.OK) => {
        val query = sqlu"DELETE FROM album WHERE id = ${album.id}"
        DBManager.db.run(query).onComplete {
          case Success(v) => update()
          case Failure(v) => ()
        }
      }
      case _ => ()
    }
  }
}
