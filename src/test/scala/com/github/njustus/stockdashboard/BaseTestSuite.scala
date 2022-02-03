package com.github.njustus.stockdashboard

import java.nio.file.*
import org.scalatest.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import com.typesafe.scalalogging.LazyLogging

import scala.io.{Codec, Source}
import io.circe.Decoder

import java.io.{InputStreamReader, Reader}

trait BaseTestSuite
  extends AnyFlatSpec
    with Matchers
    with LazyLogging:

  implicit val codec: Codec = Codec.UTF8

  extension [A](result: Decoder.Result[A])
    def get: A = result match
      case Left(ex) => throw ex
      case Right(v) => v

  def resourceToString(resource:String): String =
    Source.fromResource(resource).mkString    

  def resourceReader(resource:String): Reader =
    Source.fromResource(resource).reader()
  
  def resourcePath(resource:String):Path =
    logger.info(s"searching for resource $resource")
    Paths.get(getClass.getResource(resource).toURI)
