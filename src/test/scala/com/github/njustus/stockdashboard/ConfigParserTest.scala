package com.github.njustus.stockdashboard

import com.github.njustus.stockdashboard.config.*
import io.circe.Encoder
import io.circe.Json
import io.circe.generic.semiauto.*

import java.nio.file.Path

class ConfigParserTest extends BaseTestSuite:
  import ConfigParser.*

  implicit val stockEncoder: Encoder.AsObject[WatchedStock] = deriveEncoder[WatchedStock]
  implicit val encoder: Encoder.AsObject[WatchedStocks] = deriveEncoder[WatchedStocks]

  "The ConfigParser" should "parse yaml files" in {
    val json: Json = ConfigParser.readYaml(resourceReader("watched-stocks.yaml")).get
    json shouldNot be(null)
  }

  it should "parse BigDecimals" in {
    val jsonStr = """{
      "isin" : "test",
      "quantity" : 23.374890,
      "alias" : "MSCI All Country World"
    }"""

    val json = io.circe.parser.parse(jsonStr).getOrElse(throw new RuntimeException)
    json.as[WatchedStock].get
  }

  it should "parse WatchedStocks" in {
    val stocks = ConfigParser.readUserConfig(resourcePath("/watched-stocks.yaml")).get
    val expected = WatchedStocks(List(
      WatchedStock("123456", 5, None),
      WatchedStock("FR00542", 155, Some("MSCI World"))
    ))

    stocks shouldBe(expected)
  }

  it should "parse AppConfig's" in {
    val appConfig = ConfigParser.readAppConfig("example-config.yaml").get

    appConfig shouldBe (AppConfig(
      "http://google.de",
      "/stockInfo?field=Basic",
      "/exchangeData?field=ExchangesV2",
      Some("Staging")
    ))
  }
