package com.redbubble.finchtemplate.fetch.people

import com.redbubble.finchtemplate.model.Person
import com.redbubble.finchtemplate.services.people.PersonId
import fetch.Fetch

object PeopleFetcher {
  val allPeopleFetch: Fetch[Seq[Person]] = {
    val id = ()
    Fetch(id)(PeopleDataSource)
  }

  def personFetch(personId: PersonId): Fetch[Option[Person]] = {
    val id = PeopleId(personId)
    Fetch(id)(PersonDetailsDataSource)
  }
}
