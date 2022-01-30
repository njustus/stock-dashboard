package com.github.njustus.stockdashboard

import com.github.njustus.stockdashboard.dtos.{ExchangeData, StockInfo}
import com.github.njustus.stockdashboard.parser.CBParsers
import org.scalatest.*
import org.scalatest.flatspec.AnyFlatSpec
import com.typesafe.scalalogging.LazyLogging

import scala.io.Codec
import org.scalatest.matchers.should.Matchers

import java.time.*
import scala.io.Codec
import cats.syntax.option._

class CBParsersTest extends BaseTestSuite:

  "The CBParsers" should "parse price response from /web-financialinfo-service/api/marketdata/etfs?id=[ISIN]&field=PriceV2" in {
    val sampleResponse = resourceToString("price-response.json")
    logger.info("parsing this response: {}", sampleResponse)
    val excData = CBParsers.extractExchangeData(CBParsers.tojsonUnsafe(sampleResponse)).get

    val expected = ExchangeData(
      "FR0010315770",
      259.38,
      OffsetDateTime.of(LocalDateTime.of(2022, 1, 28, 9, 0,0), ZoneOffset.UTC)
    )
    excData shouldBe(expected)
  }

  it should "parse stock information response from /web-financialinfo-service/api/marketdata/etfs?id=FR0010315770&field=BasicV1" in {
    val info = resourceToString("basic-response.json")
    logger.info("parsing this response: {}", info)
    val excData = CBParsers.extractStockInfo(CBParsers.tojsonUnsafe(info)).get

    val expected = StockInfo(
      "LYXOR MSCI WORLD UCITS ETF - EUR DIS",
      "FR0010315770",
      "LYX0AG",
      0.3,
      "Aktien International".some,
      "Aktienfonds".some
    )

    excData shouldBe(expected)
  }
