package com.github.njustus.stockdashboard

import com.github.njustus.stockdashboard.dtos.*
import cats.effect.IO
import com.github.njustus.stockdashboard.config.WatchedStock

import java.text.NumberFormat
import java.util.Formatter.BigDecimalLayoutForm
import java.util.Locale

object StockRecordPrinter:
  val format = NumberFormat.getCurrencyInstance(Locale.GERMANY)

  def currency: BigDecimal => String = format.format

  def print(record: StockRecord): String =
    val StockRecord(ws, info, exchange) = record
    s"""${info.name} (${info.wkn}) - ${currency(exchange.price)} - ${currency(record.total)} (${ws.quantity})"""

  def printRecords(records: List[StockRecord]): String =
    val total = records.map(_.total).sum
    "Name WKN Price Total (Pieces)\n"+
      records.map(print).mkString("\n")+
    s"\nTotal: ${currency(total)}"
