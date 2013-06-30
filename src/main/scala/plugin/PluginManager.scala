package com.bfritz.rockcollector.plugin

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.slf4j.Logging

import scala.collection.JavaConversions._

/**
 * Load and start plugins.
 */
object PluginManager extends Logging {
  val conf = ConfigFactory.load().getConfig("rock-collector")

  def startPlugins() {
    logger.info("Loading plugins...")
    for(pluginClass <- conf.getStringList("plugins")) {
      val plugin: GemPlugin = loadPlugin(pluginClass)

      logger.debug(s"Starting plugin $plugin.name($pluginClass)")
      plugin.start()
    }

    def loadPlugin(pluginClass: String): GemPlugin = {
      logger.info(s"Loading plugin $pluginClass")
      val clazz = Class.forName(pluginClass)
      val const = clazz.getConstructors()(0)
      const.newInstance().asInstanceOf[GemPlugin]
    }
  }
}
