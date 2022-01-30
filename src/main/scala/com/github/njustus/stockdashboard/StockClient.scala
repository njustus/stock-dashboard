package com.github.njustus.stockdashboard

import cats.effect.*
import cats.syntax.all.*
import com.github.njustus.stockdashboard.config.AppConfig
import com.github.njustus.stockdashboard.dtos.{ExchangeData, StockInfo}
import com.github.njustus.stockdashboard.parser.CBParsers
import org.http4s.client.Client
import org.http4s.EntityDecoder
import org.http4s.implicits.*
import org.http4s.circe.*
import io.circe.Json
import com.typesafe.scalalogging.LazyLogging

class StockClient[F[_]:Async](httpClient: Client[F])(appConfig: AppConfig) extends LazyLogging:
  implicit def jsonDecoder[F[_]:Async]: EntityDecoder[F, Json] = jsonOf

  def getStockInfo(isin: String): F[StockInfo] =
    val uri = appConfig.stockInfoUri(isin)
    logger.info("getting StockInfo for {} from {}", isin, uri)

    httpClient.expect[Json](uri)
      .map(CBParsers.extractStockInfo)
      .flatMap(Sync[F].fromEither)

  def getExchangeData(isin: String): F[ExchangeData] =
    val uri = appConfig.exchangeDataUri(isin)
    logger.info("getting ExchangeData for {} from {}", isin, uri)

    httpClient.expect[Json](uri)
      .map(CBParsers.extractExchangeData)
      .flatMap(Sync[F].fromEither)
