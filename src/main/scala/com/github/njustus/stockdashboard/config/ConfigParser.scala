package com.github.njustus.stockdashboard.config

import java.nio.file.Path
import scala.io.Source

object ConfigParser {
  import io.circe.*
  import io.circe.generic.semiauto._
  import io.circe.yaml.parser

  implicit val watchedStockDecoder: Decoder[WatchedStock] = deriveDecoder[WatchedStock]
  implicit val watchedStocksDecoder: Decoder[WatchedStocks] = deriveDecoder[WatchedStocks]

  def readYaml(path: Path): Decoder.Result[Json] =
    parser.parse(Source.fromFile(path.toFile).reader()) match
      case Right(js) => Right(js)
      case Left(failure) => Left(DecodingFailure.fromThrowable(failure, List.empty))

  def readUserConfig(path: Path): Decoder.Result[WatchedStocks] =
    readYaml(path).flatMap(json => json.as[WatchedStocks])
}
