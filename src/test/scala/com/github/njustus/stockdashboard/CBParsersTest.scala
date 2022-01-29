package com.github.njustus.stockdashboard

import com.github.njustus.stockdashboard.dtos.ExchangeData
import com.github.njustus.stockdashboard.parser.CBParsers
import org.scalatest.*
import org.scalatest.flatspec.AnyFlatSpec
import com.typesafe.scalalogging.LazyLogging
import scala.io.Codec
import org.scalatest.matchers.should.Matchers

import java.time.*
import scala.io.Codec

class CBParsersTest extends BaseTestSuite:
  
  "The CBParsers" should "parse price response from /web-financialinfo-service/api/marketdata/etfs?id=[ISIN]&field=PriceV2" in {
    val sampleResponse = scala.io.Source.fromResource("price-response.json").mkString
    logger.info("parsing this response: {}", sampleResponse)
    val excData = CBParsers.extractExchangeData(CBParsers.tojsonUnsafe(sampleResponse)).get

    val expected = ExchangeData(
      "FR0010315770",
      259.38,
      OffsetDateTime.of(LocalDateTime.of(2022, 1, 28, 9, 0,0), ZoneOffset.UTC)
    )
    excData shouldBe(expected)
  }
