package com.redbubble.finchtemplate.util.http

import java.net.URL

import com.redbubble.finchtemplate.util.http.ClientOps.configureDefaults
import com.twitter.finagle.Http.Client

abstract class FinchTemplateHttpClient(baseUrl: URL) extends featherbed.Client(baseUrl)

object FinchTemplateHttpClient {
  /**
    * Default client configuration, including timeouts, retries, etc.
    */
  def defaultClient(baseUrl: URL): featherbed.Client =
    new FinchTemplateHttpClient(baseUrl) {
      override protected def clientTransform(client: Client) = configureDefaults(client)
    }
}
