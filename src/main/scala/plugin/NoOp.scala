package com.bfritz.rockcollector.plugin

import com.typesafe.scalalogging.slf4j.Logging

class NoOp extends GemPlugin with Logging {

  def name: String = "NoOp"

  def start() {
    logger.info(s"$name plugin started.")
  }
}
