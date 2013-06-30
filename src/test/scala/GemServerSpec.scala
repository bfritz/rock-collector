package com.bfritz.rockcollector

import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelFutureListener

import org.scalatest._
import org.scalatest.matchers._

class GemServerSpec extends FreeSpec with ShouldMatchers {
  "GemServer" - {
    "should be startable" in {
        def echoStarted(cf: ChannelFuture) { println("Started!") }
        val channelFuture = GemServer.startGemListener(9999)
        onStart(channelFuture, echoStarted)
        waitAndAssertSuccess(channelFuture)
    }
  }

  def waitAndAssertSuccess(cf: ChannelFuture) {
    cf.await
    cf.isDone should be (true)
    cf.isCancelled should be (false)
    cf.isSuccess should be (true)
  }

  def onStart(cf: ChannelFuture, func: ChannelFuture => Unit) {
    cf.addListener(new ChannelFutureListener() {
      def operationComplete(future: ChannelFuture) {
        if (future.isSuccess) {
          func(future)
        } else {
          future.channel.pipeline.fireExceptionCaught(future.cause)
        }
      }
    })
  }
}
