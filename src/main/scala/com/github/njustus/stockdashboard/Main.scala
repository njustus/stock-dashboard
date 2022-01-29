package com.github.njustus.stockdashboard

import cats.effect.{ExitCode, IO, IOApp}

object Main extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    StockdashboardServer.stream[IO].compile.drain.as(ExitCode.Success)
}
