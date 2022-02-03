package com.github.njustus.stockdashboard.dtos

case class StockInfo(val name: String,
                     isin: String,
                     val wkn:String,
                     ter: Option[BigDecimal],
                     val investmentFocus: Option[String],
                     val investmentType: Option[String]) {

  def getTer:String = ter.map(_.toString).getOrElse("n.a.")
}
