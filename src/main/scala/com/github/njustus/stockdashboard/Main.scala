package com.github.njustus.stockdashboard

import cats.syntax.*
import cats.effect.syntax.all.*
import cats.effect.{ExitCode, IO, IOApp, Resource}
import com.github.njustus.stockdashboard.config.{AppConfig, ConfigParser, WatchedStocks}

import java.nio.file.Paths
import scala.io.{BufferedSource, Source}
import org.http4s.ember.client.EmberClientBuilder

object Main extends IOApp {
  val builder = new TemplateBuilder

  def appConfig: IO[AppConfig] =
    for
      configPath <- IO.delay(Paths.get(getClass.getResource("/config.yaml").toURI))
      result <- IO.fromEither(ConfigParser.readAppConfig(configPath))
    yield result

  def generateHtml(userConfig: WatchedStocks): String =
    val first = userConfig.stocks.head
    builder.render("index", Map("isin" -> first.isin, "quantity" -> first.quantity))

  def run(args: List[String]): IO[ExitCode] =
    EmberClientBuilder.default[IO].build.use { httpClient =>
      for
        config <- appConfig
        userConfig <- IO.fromEither(ConfigParser.readUserConfigFromDefaultPath)
        _ <- IO.println(generateHtml(userConfig))
//        stockClient = new StockClient[IO](httpClient)(config)
//        processor = new ConfigProcessor(stockClient)(config)
//        latestStockData <- processor.readLatestStockData
//        prettied = StockRecordPrinter.printRecords(latestStockData)
//        _ <- IO.println(prettied)
      yield ExitCode.Success
    }
}
