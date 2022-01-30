package com.github.njustus.stockdashboard

import com.github.njustus.stockdashboard.config.*
import io.circe.Encoder
import io.circe.Json
import io.circe.generic.semiauto.*

import java.nio.file.Path

class ConfigParserTest extends BaseTestSuite:
  import ConfigParser.*
  val filePath: Path = resourcePath("/watched-stocks.yaml")
  val appConfigPath: Path = resourcePath("/example-config.yaml")

  implicit val stockEncoder: Encoder.AsObject[WatchedStock] = deriveEncoder[WatchedStock]
  implicit val encoder: Encoder.AsObject[WatchedStocks] = deriveEncoder[WatchedStocks]

  "The ConfigParser" should "parse yaml files" in {
    val json: Json = ConfigParser.readYaml(filePath).get
    json shouldNot be(null)
  }

  it should "parse WatchedStocks" in {
    val stocks = ConfigParser.readUserConfig(filePath).get
    val expected = WatchedStocks(List(
      WatchedStock("123456", 5, None),
      WatchedStock("FR00542", 155, Some("MSCI World"))
    ))

    stocks shouldBe(expected)
  }
  
  it should "parse AppConfig's" in {
    val appConfig = ConfigParser.readAppConfig(appConfigPath).get
    
    appConfig shouldBe (AppConfig(
      "http://google.de",
      "/stockInfo?field=Basic",
      "/exchangeData?field=ExchangesV2"
    ))
  }
