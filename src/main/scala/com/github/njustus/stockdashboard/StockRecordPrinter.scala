package com.github.njustus.stockdashboard

import com.github.njustus.stockdashboard.dtos.*
import cats.effect.IO
import com.github.njustus.stockdashboard.config.WatchedStock

object StockRecordPrinter:
  def print(record: StockRecord): String =
    val StockRecord(ws, info, exchange) = record
    s"""${info.name} (${info.wkn}) - ${exchange.price} - ${record.total} EUR (${ws.quantity})"""

  def printRecords(records: List[StockRecord]): String =
    val total = records.map(_.total).sum
    "Name WKN Price Total (Pieces)\n"+
      records.map(print).mkString("\n")+
    s"\nTotal: ${total} EUR"
