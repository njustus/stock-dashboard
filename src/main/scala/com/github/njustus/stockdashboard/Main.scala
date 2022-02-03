package com.github.njustus.stockdashboard

import cats.syntax.*
import cats.effect.syntax.all.*
import cats.effect.{ExitCode, IO, IOApp, Resource}
import com.github.njustus.stockdashboard.config.{AppConfig, ConfigParser, WatchedStocks}

import java.nio.file.{Files, Paths}
import scala.io.{BufferedSource, Source}
import org.http4s.ember.client.EmberClientBuilder

import java.nio.charset.StandardCharsets

object Main extends IOApp {

  def appConfig: IO[AppConfig] =
    for
      result <- IO.fromEither(ConfigParser.readAppConfig("config.yaml"))
    yield result

  def run(args: List[String]): IO[ExitCode] =
    val builder = new TemplateBuilder
    EmberClientBuilder.default[IO].build.use { httpClient =>
      for
        config <- appConfig
        stockClient = new StockClient[IO](httpClient)(config)
        processor = new ConfigProcessor(stockClient)(config)
        routes = new StockRoutes(builder, processor)
        serverResource = StockdashboardServer.buildServer(routes)
        _ <- serverResource.use(_ => IO.never)
      yield ExitCode.Success
    }
}
