package com.github.njustus.stockdashboard

import cats.effect.Async
import com.github.njustus.stockdashboard.config.AppConfig
import org.http4s.client.Client

class StockClient[F[_]:Async](httpClient: Client[F])(implicit config:AppConfig):
  def get = ???
