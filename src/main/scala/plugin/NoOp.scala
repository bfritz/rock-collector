package com.bfritz.rockcollector.plugin

import com.google.common.eventbus.EventBus

import com.typesafe.scalalogging.slf4j.Logging

class NoOp(bus: EventBus) extends GemPlugin with Logging {

  def name: String = "NoOp"

  def start() {
    logger.info(s"$name plugin started.")
    bus.register(this)
  }
}
