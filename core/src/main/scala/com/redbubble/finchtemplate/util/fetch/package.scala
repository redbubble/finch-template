package com.redbubble.finchtemplate.util

import com.redbubble.finchtemplate.util.cache.Cache.fetchedObjectCache
import com.redbubble.finchtemplate.util.async.futurePool
import com.redbubble.util.fetch.FetcherRunner

package object fetch {
  lazy val runner: FetcherRunner = FetcherRunner(fetchedObjectCache)(futurePool)
}
