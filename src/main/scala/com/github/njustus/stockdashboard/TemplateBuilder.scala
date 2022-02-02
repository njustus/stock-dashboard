package com.github.njustus.stockdashboard

import de.neuland.pug4j.{Pug4J, PugConfiguration}
import de.neuland.pug4j.template.ClasspathTemplateLoader
import scala.jdk.CollectionConverters.*

class TemplateBuilder {
  private val loader = new ClasspathTemplateLoader("views")
  private val config = new PugConfiguration()
  config.setCaching(false)
  config.setTemplateLoader(loader)
  config.setPrettyPrint(true)
  config.setMode(Pug4J.Mode.XHTML)

  def render(templateName: String, args: Map[String, AnyRef]): String =
    val template = config.getTemplate(templateName)
    config.renderTemplate(template, args.asJava)
}
