package com.redbubble.finchtemplate.api

import com.redbubble.finchtemplate.api.v1.health.HealthApi.healthApi
import com.redbubble.finchtemplate.api.v1.people.PeopleApi.peopleApi

package object v1 {
  val api = "v1" :: (peopleApi :+: healthApi)
}
