package com.github.njustus.stockdashboard.config

case class WatchedStock(isin: String,
                        quantity: Long,
                        alias: Option[String])

case class WatchedStocks(stocks: List[WatchedStock])
