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
  val builder = new TemplateBuilder

  def appConfig: IO[AppConfig] =
    for
      configPath <- IO.delay(Paths.get(getClass.getResource("/config.yaml").toURI))
      result <- IO.fromEither(ConfigParser.readAppConfig(configPath))
    yield result

  def generateHtml(userConfig: WatchedStocks): String =
    val first = userConfig.stocks.head
    builder.render("index", Map("stock" -> first))

  def run(args: List[String]): IO[ExitCode] =
    EmberClientBuilder.default[IO].build.use { httpClient =>
      for
        config <- appConfig
        userConfig <- IO.fromEither(ConfigParser.readUserConfigFromDefaultPath)
        html = generateHtml(userConfig)
        _ <- IO.delay {
          val writer = Files.newBufferedWriter(Paths.get("index.html"), StandardCharsets.UTF_8)
          writer.write(html)
          writer.close()
        }
        _ <- IO.println("index written")
//        stockClient = new StockClient[IO](httpClient)(config)
//        processor = new ConfigProcessor(stockClient)(config)
//        latestStockData <- processor.readLatestStockData
//        prettied = StockRecordPrinter.printRecords(latestStockData)
//        _ <- IO.println(prettied)
      yield ExitCode.Success
    }
}
