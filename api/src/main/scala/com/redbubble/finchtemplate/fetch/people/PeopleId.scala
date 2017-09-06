package com.redbubble.finchtemplate.fetch.people

import com.redbubble.finchtemplate.model.people.PersonId

final case class PeopleId(personId: PersonId) {
  val identity: String = s"PeopleId-$personId"
}
