package com.github.njustus.stockdashboard

import cats.{Applicative, Traverse}
import cats.implicits.*
import cats.effect.{Async, IO}
import cats.effect.syntax.all.*
import com.github.njustus.stockdashboard.config.{AppConfig, ConfigParser, WatchedStock, WatchedStocks}
import com.github.njustus.stockdashboard.dtos.StockRecord
import com.typesafe.scalalogging.LazyLogging
import org.http4s.client.Client

class ConfigProcessor(stockClient: StockClient[IO])(config:AppConfig) extends LazyLogging:
  def getData(ws:WatchedStock): IO[StockRecord] =
    for
      stockInfo <- stockClient.getStockInfo(ws.isin)
      exchanges <- stockClient.getExchangeData(ws.isin)
    yield StockRecord(ws, stockInfo, exchanges)

  def readLatestStockData: IO[List[StockRecord]] =
    for
      userConfig: WatchedStocks <- Async[IO].fromEither(ConfigParser.readUserConfigFromDefaultPath)
      stockData: List[StockRecord] <- IO.parSequenceN(4)(userConfig.stocks.map(getData))
    yield stockData

