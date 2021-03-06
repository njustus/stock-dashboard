package com.github.njustus.stockdashboard.config

import java.io.{InputStreamReader, Reader}
import java.nio.file.{Path, Paths}
import scala.io.Source
import io.circe.*
import io.circe.generic.semiauto.*
import io.circe.yaml.parser
import com.typesafe.scalalogging.LazyLogging

trait ConfigDecoders:
  implicit val appConfigDecoder: Decoder[AppConfig] = deriveDecoder[AppConfig]
  implicit val watchedStockDecoder: Decoder[WatchedStock] = deriveDecoder[WatchedStock]
  implicit val watchedStocksDecoder: Decoder[WatchedStocks] = deriveDecoder[WatchedStocks]

object ConfigParser extends ConfigDecoders with LazyLogging:
  val DEFAULT_USER_PATH: Path = Paths.get(System.getProperty("user.home")).resolve(".stock-dashboard.yaml")

  def readYaml(reader: Reader) = parser.parse(reader) match
      case Right(js) => Right(js)
      case Left(failure) => Left(DecodingFailure.fromThrowable(failure, List.empty))

  def readUserConfig(path: Path): Decoder.Result[WatchedStocks] =
    readYaml(Source.fromFile(path.toFile).reader()).flatMap(json => json.as[WatchedStocks])

  def readAppConfig(resourceName: String): Decoder.Result[AppConfig] =
    readYaml(Source.fromResource(resourceName).reader()).flatMap(json => json.as[AppConfig])

  def readUserConfigFromDefaultPath: Decoder.Result[WatchedStocks] =
    logger.info("reading user-config from {}", DEFAULT_USER_PATH)
    readUserConfig(DEFAULT_USER_PATH)
