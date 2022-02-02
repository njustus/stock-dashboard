package com.github.njustus.stockdashboard

import cats.effect.kernel.Sync
import cats.{Applicative, Monad}
import cats.effect.{Async, IO, Resource}
import cats.syntax.all.*
import com.comcast.ip4s.*
import com.github.njustus.stockdashboard.parser.CBParsers
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
import org.http4s.server.Server

import java.io.File
import java.time.*
import java.time.format.DateTimeFormatter

object StockdashboardServer {
  def buildServer(routes: StockRoutes): Resource[IO, Server] =
    val httpApp  = (routes.routes).orNotFound
    val serverResource: Resource[IO, Server] = EmberServerBuilder.default[IO].withHost(ipv4"0.0.0.0").withPort(port"8080").withHttpApp(httpApp).build
    serverResource
}
