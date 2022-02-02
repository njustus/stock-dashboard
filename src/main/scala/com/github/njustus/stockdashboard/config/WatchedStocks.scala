package com.github.njustus.stockdashboard.config

case class WatchedStock(val isin: String,
                        val quantity: BigDecimal,
                        val alias: Option[String])

case class WatchedStocks(stocks: List[WatchedStock])
