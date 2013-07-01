package com.bfritz.rockcollector

import com.bfritz.rockcollector.event.GemEvent
import com.bfritz.rockcollector.plugin.PluginManager

import com.google.common.eventbus.EventBus

import com.typesafe.scalalogging.slf4j.Logging

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.{
  ChannelFuture,
  ChannelFutureListener,
  ChannelHandlerContext,
  ChannelInitializer,
  SimpleChannelInboundHandler}
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.http.{
  HttpRequest,
  HttpRequestDecoder,
  QueryStringDecoder}
import io.netty.handler.logging.{LoggingHandler, LogLevel}

import java.net.InetSocketAddress

import org.joda.time.DateTime

/**
 * Netty server that listens for updates from the GEM via TCP socket.
 * Loosely based on Netty
 * <a href="https://github.com/netty/netty/blob/master/example/src/main/java/io/netty/example/telnet/TelnetServer.java">TelnetServer</a>
 * example.
 */
object GemServer extends App with Logging {
  val port = Option(System.getenv("PORT")).map(_.toInt).getOrElse(8089)
  val bus = startEventBus
  val channelFuture = startGemListener(port)
  channelFuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE)

  PluginManager.startPlugins(bus)

  val cf = channelFuture.sync   // start the server
  cf.channel.closeFuture.sync   // wait until socket is closed

  def startEventBus(): EventBus = {
    logger.info(s"Starting event bus.")
    new EventBus()
  }

  def startGemListener(port: Int): ChannelFuture = {
    logger.info(s"Starting GemServer on port $port.")
    val bossGroup, workerGroup = new NioEventLoopGroup()
    val socket = new InetSocketAddress(port)
    new ServerBootstrap()
      .group(bossGroup, workerGroup)
      .channel(classOf[NioServerSocketChannel])
      .handler(new LoggingHandler(LogLevel.INFO))
      .childHandler(new GemServerInitializer())
      .bind(socket)
  }

  class GemServerInitializer extends ChannelInitializer[SocketChannel] {
    override def initChannel(ch: SocketChannel) {
      ch.pipeline()
        .addLast("decoder", new HttpRequestDecoder())
        .addLast("handler", new GemServerHandler())
    }
  }

  class GemServerHandler extends SimpleChannelInboundHandler[HttpRequest] {
    override def messageReceived(ctx: ChannelHandlerContext, req: HttpRequest) {
      val ts = DateTime.now
      val qs = new QueryStringDecoder(req.getUri)
      val serialNo = qs.parameters.get("SN").head
      bus.post(GemEvent(ts, serialNo))
    }
  }
}
