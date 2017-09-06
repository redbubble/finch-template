package com.redbubble.finchtemplate.util.log

import com.redbubble.finchtemplate.util.config.Config
import com.redbubble.finchtemplate.util.async.futurePool
import com.redbubble.util.log.Logger

trait CoreLogger {
  final lazy val log = new Logger(Config.coreLoggerName)(futurePool)
}

object CoreLogger extends CoreLogger
