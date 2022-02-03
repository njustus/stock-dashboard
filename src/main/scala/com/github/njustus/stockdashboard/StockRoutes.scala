package com.github.njustus.stockdashboard

import org.http4s.{Entity, EntityEncoder, Header, HttpRoutes}
import org.http4s.MediaType
import org.http4s.headers
import cats.effect.IO
import com.github.njustus.stockdashboard.dtos.StockRecord
import com.typesafe.scalalogging.LazyLogging
import org.http4s.dsl.Http4sDsl
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.*
import io.circe.syntax.*
import io.circe.parser

import java.io.File
import scala.io.Source

class StockRoutes(templateBuilder: TemplateBuilder,
                  configProcessor: ConfigProcessor) extends Http4sDsl[IO] with LazyLogging:

  implicit val recordEncoder: Encoder[StockRecord] = deriveEncoder[StockRecord]
  implicit val recordDecoder: Decoder[StockRecord] = deriveDecoder[StockRecord]

  def stockDataSample: IO[List[StockRecord]] = IO.delay {
      io.circe.parser.decode[List[StockRecord]](
        Source.fromFile(new File(System.getProperty("user.dir"), "response-example.json")).getLines().mkString
      ).getOrElse(throw IllegalStateException(s"example file expected"))
    }

  def routes = HttpRoutes.of[IO] {
    case GET -> Root =>
      for
        stocks <- stockDataSample
        stock = stocks.head
        html <- IO.delay(templateBuilder.render("index", Map("stock" -> stock)))
        resp <- Ok(html, headers.`Content-Type`(MediaType.unsafeParse("text/html")))
      yield resp
  }
