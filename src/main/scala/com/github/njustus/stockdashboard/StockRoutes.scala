package com.github.njustus.stockdashboard

import org.http4s.{Entity, EntityEncoder, Header, HttpRoutes}
import org.http4s.MediaType
import org.http4s.headers
import cats.effect.IO
import com.typesafe.scalalogging.LazyLogging
import org.http4s.dsl.Http4sDsl

class StockRoutes(templateBuilder: TemplateBuilder,
                  configProcessor: ConfigProcessor) extends Http4sDsl[IO] with LazyLogging:

  def routes = HttpRoutes.of[IO] {
    case GET -> Root =>
      for
        stocks <- configProcessor.readLatestStockData
        stock = stocks.head
        html <- IO.delay(templateBuilder.render("index", Map("stock" -> stock)))
        resp <- Ok(html, headers.`Content-Type`(MediaType.unsafeParse("text/html")))
      yield resp
  }
