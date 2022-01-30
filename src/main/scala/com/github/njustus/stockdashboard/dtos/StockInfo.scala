package com.github.njustus.stockdashboard.dtos

case class StockInfo(name: String,
                     isin: String,
                     wkn:String,
                     ter: BigDecimal,
                     investmentFocus: Option[String],
                     investmentType: Option[String])
