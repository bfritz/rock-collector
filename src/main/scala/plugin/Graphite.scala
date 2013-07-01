package com.bfritz.rockcollector.plugin

import com.bfritz.rockcollector.GemServer.ChannelRegex

import com.bfritz.rockcollector.event.GemEvent

import com.google.common.base.Charsets.UTF_8
import com.google.common.eventbus.{EventBus, Subscribe}

import com.typesafe.scalalogging.slf4j.Logging

import java.io.{BufferedWriter, OutputStreamWriter, Writer}
import java.net.InetSocketAddress

import javax.net.SocketFactory

import resource._

/**
 * Write GEM events to Graphite Carbon server using line protocol.
 */
class Graphite(bus: EventBus) extends GemPlugin with Logging {

  val graphite = new InetSocketAddress("localhost", 2003)
  val socketFactory = SocketFactory.getDefault

  def name: String = "Graphite"

  def start() {
    logger.info(s"$name plugin started.")
    bus.register(this)
  }

  @Subscribe
  def onMessage(event: GemEvent) {
    val ts: Long = System.currentTimeMillis / 1000L
    for (s <- managed(socketFactory.createSocket(graphite.getAddress, graphite.getPort));
        w <- managed(new BufferedWriter(new OutputStreamWriter(s.getOutputStream, UTF_8)))) {
      for ((k, v) <- event.values) {
        k match {
          case ChannelRegex(chNo) =>
            send(w, event, ts, s"channel.$chNo", v)
          case _ =>
        }
      }
      send(w, event, ts, "volts", event.values("volts"))
    }
  }

  def send(w: Writer, event: GemEvent, seconds: Long, metric: String, value: Number) {
    val line = s"greeneye.${event.sn}.$metric $value $seconds\n"
    logger.trace(line.trim)
    w.write(line)
  }
}
