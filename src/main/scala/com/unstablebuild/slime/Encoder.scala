package com.unstablebuild.slime

import ch.qos.logback.classic.spi.LoggingEvent
import ch.qos.logback.core.Context
import ch.qos.logback.core.encoder.{Encoder => LogbackEncoder}
import ch.qos.logback.core.status.Status
import com.unstablebuild.slime.format.Text

import scala.collection.JavaConverters._

class Encoder extends LogbackEncoder[LoggingEvent] {

  val debug = false

  var context: Context = _

  var format: Format = new Text

  var fields: Seq[String] = Seq("level", "message")

  val fieldExtractors: Map[String, LoggingEvent => Value] =
    Map(
      "level" -> (e => StringValue(e.getLevel.toString)),
      "message" -> (e => StringValue(e.getFormattedMessage)),
      "thread" -> (e => StringValue(e.getThreadName)),
      "logger" -> (e => StringValue(e.getLoggerName)),
      "mdc" -> (e => NestedValue(e.getMDCPropertyMap.asScala.mapValues(StringValue).toSeq)),
      "timestamp" -> (e => NumberValue(e.getTimeStamp))
    )

  override def encode(event: LoggingEvent): Array[Byte] = {
    if (debug) println("encode " + event + " [" + event.getClass + "]")

    val encodedData = event.getMarker match {
      case mm: AnnotationMarker =>
        if (debug) println("my marker " + mm.annotations)
        mm.encoded
      case _ =>
        if (debug) println("unknown marker")
        Seq.empty
    }

    val baseValues = fields.flatMap(f => fieldExtractors.get(f).map(extract => f -> extract(event)))

    format.format(baseValues ++ encodedData)
  }

  override def headerBytes(): Array[Byte] = {
    if (debug) println("headerBytes")
    Array.emptyByteArray
  }

  override def footerBytes(): Array[Byte] = {
    if (debug) println("footerBytes")
    Array.emptyByteArray
  }

  override def stop(): Unit = {
    if (debug) println("stop")
  }

  override def isStarted: Boolean = {
    if (debug) println("isStarted")
    true
  }

  override def start(): Unit = {
    if (debug) println("start")
  }

  override def addInfo(msg: String): Unit = {
    if (debug) println("addInfo " + msg)
  }

  override def addInfo(msg: String, ex: Throwable): Unit = {
    if (debug) println("addInfo " + msg)
  }

  override def addWarn(msg: String): Unit = {
    if (debug) println("addWarn " + msg)
  }

  override def addWarn(msg: String, ex: Throwable): Unit = {
    if (debug) println("addWarn " + msg)
  }

  override def addError(msg: String): Unit = {
    if (debug) println("addError " + msg)
  }

  override def addError(msg: String, ex: Throwable): Unit = {
    if (debug) println("addError " + msg)
  }

  override def addStatus(status: Status): Unit = {
    if (debug) println("addStatus " + status)
  }

  override def getContext: Context = {
    if (debug) println("getContext")
    context
  }

  override def setContext(context: Context): Unit = {
    if (debug) println("setContext " + context)
    this.context = context
  }

  def setFormat(format: Format): Unit = {
    if (debug) println(s"setFormat ${format.getClass}")
    this.format = format
  }

  def setFields(allFields: String): Unit = {
    if (debug) println("setFields " + allFields)
    this.fields = allFields.split(",").map(_.toLowerCase.trim)
  }

}
