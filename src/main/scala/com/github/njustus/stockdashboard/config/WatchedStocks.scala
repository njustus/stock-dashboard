package com.github.njustus.stockdashboard.config

case class WatchedStock(isin: String,
                        quantity: BigDecimal,
                        alias: Option[String])

case class WatchedStocks(stocks: List[WatchedStock])
