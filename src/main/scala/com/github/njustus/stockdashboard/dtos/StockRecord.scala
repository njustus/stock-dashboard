package com.github.njustus.stockdashboard.dtos

import com.github.njustus.stockdashboard.config.WatchedStock

case class StockRecord(val ws: WatchedStock,
                       val info: StockInfo,
                       val exchange:ExchangeData):
  assert(ws.isin == info.isin, "Expected 'WatchedStock#isin' is equal to 'StockInfo#isin'")

  val isin: String = info.isin
  val total: BigDecimal = exchange.price *  ws.quantity
