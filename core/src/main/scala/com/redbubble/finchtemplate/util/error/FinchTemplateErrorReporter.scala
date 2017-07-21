package com.redbubble.finchtemplate.util.error

import com.redbubble.finchtemplate.util.async.futurePool
import com.redbubble.finchtemplate.util.config.Config.rollbarAccessKey
import com.redbubble.finchtemplate.util.config.Environment.env
import com.redbubble.finchtemplate.util.config.{Environment, Production}
import com.redbubble.util.config.Environment
import com.redbubble.util.error._
import com.redbubble.util.http.DownstreamError
import com.twitter.finagle.{ChannelClosedException, IndividualRequestTimeoutException, ServiceTimeoutException}
import com.twitter.util.FuturePool

private final class FinchTemplateErrorReporter(
    environment: Environment, enabledEnvironments: Seq[Environment])(implicit fp: FuturePool) extends ErrorReporter {
  private val underlying = new RollbarErrorReporter(rollbarAccessKey, environment, enabledEnvironments)(fp)

  override def registerForUnhandledExceptions() = underlying.registerForUnhandledExceptions()

  def report(level: ErrorLevel, t: Throwable, extraData: Option[ExtraData] = None): Unit =
    reportLevel(t).foreach { level =>
      val extra = errorExtraData(t).map(eed => eed ++ extraData.getOrElse(Map()))
      underlying.report(level, t, extra)
    }

  private def reportLevel(t: Throwable): Option[ErrorLevel] = t match {
    // An example of not logging a 404 from a downstream service.
    case e @ DownstreamError(_, i) if e.getMessage.contains("Resource Not Found") && i.requestUrl.startsWith(env.peopleApiUrl.toString) =>
      None
    // We don't care about acquisition timeouts to downstream services, as these are usually intermittent.
    case _: ServiceTimeoutException => None
    // We don't care about individual request timeouts to downstream services, as these are usually intermittent.
    case _: IndividualRequestTimeoutException => None
    // We don't care about downstream services closing connections, as these are usually intermittent.
    case _: ChannelClosedException => None
    case _ => Some(Error)
  }

  private val DownstreamPrefix = "downstream.service"

  private def errorExtraData(t: Throwable): Option[ExtraData] = t match {
    case e: DownstreamError => Some(
      Map(
        s"$DownstreamPrefix.request.url" -> e.interaction.requestUrl,
        s"$DownstreamPrefix.request.headers" -> e.interaction.requestHeaders,
        s"$DownstreamPrefix.request.body" -> e.interaction.requestBody,
        s"$DownstreamPrefix.response.status" -> e.interaction.responseStatusCode,
        s"$DownstreamPrefix.response.body" -> e.interaction.responseBody
      )
    )
    case _ => None
  }
}

/**
  * Entry point to error reporting. Knows about which errors to log vs. not log.
  */
object FinchTemplateErrorReporter {
  val errorReporter: ErrorReporter = new FinchTemplateErrorReporter(Environment.env, Seq(Production))(futurePool)
}
