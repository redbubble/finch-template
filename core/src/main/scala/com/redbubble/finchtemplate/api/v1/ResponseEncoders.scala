package com.redbubble.finchtemplate.api.v1

import com.redbubble.finchtemplate.api.v1.people.PeopleEncoders
import com.redbubble.util.http.ErrorEncoders

trait ResponseEncoders extends ErrorEncoders with PeopleEncoders

object ResponseEncoders extends ResponseEncoders
