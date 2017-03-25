package com.redbubble.finchtemplate

import com.redbubble.finchtemplate.api.v1
import com.redbubble.finchtemplate.api.v1.FinchTemplateErrorHandler.apiErrorHandler
import com.redbubble.finchtemplate.api.v1.{FinchTemplateErrorHandler, ResponseEncoders}
import com.redbubble.finchtemplate.util.config.Config._
import com.redbubble.finchtemplate.util.config.{Development, Environment, Test}
import com.redbubble.finchtemplate.util.metrics.Metrics._
import com.redbubble.hawk.HawkAuthenticateRequestFilter
import com.redbubble.util.http.ExceptionFilter
import com.redbubble.util.http.filter.{HttpBasicAuthFilter, RequestLoggingFilter, RoutingMetricsFilter}
import com.twitter.finagle.Service
import com.twitter.finagle.http.{Request, Response}
import io.finch.Application

object ApiAuthFilter extends HawkAuthenticateRequestFilter(apiAuthenticationCredentials, whitelistedAuthPaths, serverMetrics)

object BasicAuthFilter extends HttpBasicAuthFilter(s"$systemName Security", basicAuthCredentials, basicAuthPaths, serverMetrics)

object UnhandledExceptionsFilter extends ExceptionFilter(ResponseEncoders.throwableEncode, FinchTemplateErrorHandler, serverMetrics)

object RouteMetricsFilter extends RoutingMetricsFilter(serverMetrics)

object FinchTemplateApi extends ResponseEncoders {
  private val api = v1.api

  def apiService: Service[Request, Response] = filters andThen api.handle(apiErrorHandler).toServiceAs[Application.Json]

  private def filters = {
    val baseFilters = RequestLoggingFilter andThen UnhandledExceptionsFilter andThen RouteMetricsFilter
    Environment.env match {
      case Development => baseFilters
      case Test => baseFilters
      case _ => baseFilters andThen ApiAuthFilter andThen BasicAuthFilter
    }
  }
}
