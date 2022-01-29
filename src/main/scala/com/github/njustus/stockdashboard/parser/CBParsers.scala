package com.github.njustus.stockdashboard.parser

import cats.Applicative
import com.github.njustus.stockdashboard.dtos.ExchangeData
import io.circe.{Decoder, Json, ParsingFailure, parser}

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

trait ParsingSupport:
  implicit val offsetDateTimDecoder: Decoder[OffsetDateTime] = Decoder.decodeString.map { str =>
    val stripped = str.substring(0, str.length - 2)
    val ancestor = DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(stripped)
    OffsetDateTime.from(ancestor)
  }

  def toJson(str:String): Either[ParsingFailure, Json] = parser.parse(str)

  def tojsonUnsafe(str:String): Json = toJson(str) match
    case Left(error) => throw new IllegalArgumentException("Couldn't parse provided string", error)
    case Right(js) => js

object CBParsers extends ParsingSupport:
  def extractExchangeData(json: Json): Decoder.Result[ExchangeData] =
    val cursor = json.hcursor.downN(0)
    val id = cursor.downField("Info").get[String]("ID")
    val price = cursor.downField("PriceV2").get[BigDecimal]("PRICE")
    val time = cursor.downField("PriceV2").get[OffsetDateTime]("DATETIME_PRICE")

    Applicative[Decoder.Result].map3(id, price, time)(ExchangeData.apply)
