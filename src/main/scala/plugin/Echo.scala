package com.bfritz.rockcollector.plugin

import com.bfritz.rockcollector.event.RCEvent

import com.google.common.eventbus.{Subscribe, EventBus}

import com.typesafe.scalalogging.slf4j.Logging

class Echo(bus: EventBus) extends GemPlugin with Logging {

  def name: String = "Echo"

  def start() {
    logger.info(s"$name plugin started.")
    bus.register(this)
  }

  @Subscribe
  def onMessage(event: RCEvent) {
    println(event)
  }
}
