package com.bfritz.rockcollector.event

import org.joda.time.DateTime

case class GemEvent(ts: DateTime, sn: String, values: Map[String,Number]) extends RCEvent
