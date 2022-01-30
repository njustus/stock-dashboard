package com.github.njustus.stockdashboard

import cats.syntax.*
import cats.effect.syntax.all.*
import cats.effect.{ExitCode, IO, IOApp, Resource}
import com.github.njustus.stockdashboard.config.{AppConfig, ConfigParser}

import java.nio.file.Paths
import scala.io.{BufferedSource, Source}
import org.http4s.ember.client.EmberClientBuilder

object Main extends IOApp {
  def appConfig: IO[AppConfig] =
    for
      configPath <- IO.delay(Paths.get(getClass.getResource("/config.yaml").toURI))
      result <- IO.fromEither(ConfigParser.readAppConfig(configPath))
    yield result

  def run(args: List[String]): IO[ExitCode] =
    EmberClientBuilder.default[IO].build.use { httpClient =>
      for
        config <- appConfig
        stockClient = new StockClient[IO](httpClient)(config)
        processor = new ConfigProcessor(stockClient)(config)
        latestStockData <- processor.readLatestStockData
        prettied = latestStockData.mkString("\n")
        _ <- IO.println("latest stock data:\n"+prettied)
      yield ExitCode.Success
    }
}
