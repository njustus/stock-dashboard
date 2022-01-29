package com.github.njustus.stockdashboard

import cats.effect.kernel.Sync
import cats.{Applicative, Monad}
import cats.effect.{Async, Resource}
import cats.syntax.all.*
import com.comcast.ip4s.*
import fs2.Stream
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits.*
import org.http4s.server.middleware.Logger
import org.http4s.ember.client.EmberClient
import org.http4s.client.Client
import org.http4s.circe.*
import io.circe.{Decoder, Json}
import io.circe.parser
import org.http4s.EntityDecoder

import java.io.File
import java.time.*
import java.time.format.DateTimeFormatter

object StockdashboardServer {

  val isin = "FR0010315770"

  implicit val offsetDateTimDecoder: Decoder[OffsetDateTime] = Decoder.decodeString.map { str =>
    val stripped = str.substring(0, str.length - 2)
    val ancestor = DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(stripped)
    OffsetDateTime.from(ancestor)
  }

  case class ExchangeData(isin: String,
                         price: BigDecimal,
                         time: OffsetDateTime)

  def getSampleFile =
    val src = scala.io.Source.fromFile(new File("./example.json"))
    src.getLines().mkString

  def extractExchangeData(json: Json): Decoder.Result[ExchangeData] =
    println(s"decoding json: $json")
    val cursor = json.hcursor.downN(0)
    val id = cursor.downField("Info").get[String]("ID")
    val price = cursor.downField("PriceV2").get[BigDecimal]("PRICE")
    val time = cursor.downField("PriceV2").get[OffsetDateTime]("DATETIME_PRICE")

    Applicative[Decoder.Result].map3(id, price, time)(ExchangeData.apply)

  def jsonDecoder[F[_]:Async]: EntityDecoder[F, Json] = jsonOf

  def fetchStockData[F[_]:Async](client: Client[F]) =
    val priceUri = s"https://www.consorsbank.de/web-financialinfo-service/api/marketdata/etfs?id=${isin}&field=PriceV2"
    client.expect[Json](priceUri)

  def stream[F[_]: Async]: Stream[F, Nothing] = {
    for {
      client <- Stream.resource(EmberClientBuilder.default[F].build)
      helloWorldAlg = HelloWorld.impl[F]
      jokeAlg = Jokes.impl[F](client)

//      data = Sync[F].delay(getSampleFile)
//        .map(str => parser.parse(str).flatMap(extractExchangeData))
//        .map(x => println("data fetched: "+x))
      data = fetchStockData(client)
        .map(extractExchangeData)
        .map(x => println("data fetched: "+x))

      // Combine Service Routes into an HttpApp.
      // Can also be done via a Router if you
      // want to extract a segments not checked
      // in the underlying routes.
      httpApp = (
        StockdashboardRoutes.helloWorldRoutes[F](helloWorldAlg) <+>
        StockdashboardRoutes.jokeRoutes[F](jokeAlg)
      ).orNotFound

      // With Middlewares in place
      finalHttpApp = Logger.httpApp(true, true)(httpApp)

      exitCode <- Stream.resource(
        EmberServerBuilder.default[F]
          .withHost(ipv4"0.0.0.0")
          .withPort(port"8080")
          .withHttpApp(finalHttpApp)
          .build >>
          Resource.eval(data) >>
        Resource.eval(Async[F].never)
      )
    } yield exitCode
  }.drain
}
