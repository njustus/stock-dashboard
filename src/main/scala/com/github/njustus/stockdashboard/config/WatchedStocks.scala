package com.github.njustus.stockdashboard.config

case class WatchedStock(isin: String,
                        quantity: Long)

case class WatchedStocks(stocks: List[WatchedStock])
