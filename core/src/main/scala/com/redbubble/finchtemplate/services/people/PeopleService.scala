package com.redbubble.finchtemplate.services.people

import com.redbubble.finchtemplate.fetch.people.PeopleFetcher._
import com.redbubble.finchtemplate.model.Person
import com.redbubble.util.fetch.syntax._
import com.twitter.util.Future

trait PeopleService {
  private implicit lazy val fetchRunner = com.redbubble.finchtemplate.util.fetch.runner

  final def allPeople(): Future[Seq[Person]] = {
    val fetch = allPeopleFetch
    fetch.runF
  }

  final def personDetails(personId: PersonId): Future[Option[Person]] = {
    val fetch = personFetch(personId)
    fetch.runF
  }
}

object PeopleService extends PeopleService
