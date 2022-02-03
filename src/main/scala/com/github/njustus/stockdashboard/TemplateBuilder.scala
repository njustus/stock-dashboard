package com.github.njustus.stockdashboard

import com.github.njustus.stockdashboard.config.AppConfig
import com.typesafe.scalalogging.LazyLogging
import de.neuland.pug4j.{Pug4J, PugConfiguration}
import de.neuland.pug4j.template.{FileTemplateLoader, TemplateLoader}

import java.nio.file.Paths
import scala.jdk.CollectionConverters.*

private class ResourceTemplateLoader(resourceBasePath: String) extends TemplateLoader with LazyLogging {
  import java.io.{InputStreamReader, Reader}

  override def getLastModified(name: String): Long = -1

  override def getReader(name: String): Reader =
    val path = s"""$resourceBasePath/$name"""
    logger.debug(s"resolving template $name to $path")

    val is = getClass.getClassLoader.getResourceAsStream(path)
    assume(is != null, "InputStream from $path is null")
    InputStreamReader(is)

  override def getExtension: String = "pug"

  override def getBase: String = ""
}


class TemplateBuilder(appConfig:AppConfig) {
  private val loader = {
    if(appConfig.isDevelopment)
      val basePath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "views")
      FileTemplateLoader(basePath.toString)
    else
      ResourceTemplateLoader("views")
  }
  private val config = PugConfiguration()
  config.setCaching(false)
  config.setTemplateLoader(loader)
  config.setPrettyPrint(true)
  config.setMode(Pug4J.Mode.XHTML)

  def render(templateName: String, args: Map[String, AnyRef]): String =
    val template = config.getTemplate(templateName+"."+loader.getExtension)
    config.renderTemplate(template, args.asJava)
}
