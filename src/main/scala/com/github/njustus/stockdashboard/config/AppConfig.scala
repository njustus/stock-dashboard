package com.github.njustus.stockdashboard.config

import org.http4s.implicits.uri
import org.http4s.Uri

case class AppConfig(baseUrl: String,
                     stockInfoPath: String,
                     exchangeDataPath: String,
                     environment: Option[String]):
  def stockInfoUri(isin: String): Uri = Uri.unsafeFromString(baseUrl+"/"+stockInfoPath.replaceAll("\\{isin\\}", isin))
  def exchangeDataUri(isin: String): Uri = Uri.unsafeFromString(baseUrl+"/"+exchangeDataPath.replaceAll("\\{isin\\}", isin))

  def isDevelopment: Boolean = environment.contains("DEV")
