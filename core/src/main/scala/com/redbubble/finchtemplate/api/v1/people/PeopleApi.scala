package com.redbubble.finchtemplate.api.v1.people

import com.redbubble.finchtemplate.model.Person
import com.redbubble.finchtemplate.model.people.PersonId
import com.redbubble.finchtemplate.services.people.PeopleService
import com.redbubble.util.http.Errors.notFoundError
import io.finch.{Endpoint, _}

object PeopleApi {
  val peopleApi = people :+: person

  def people: Endpoint[Seq[Person]] =
    get("v1" :: "pepople") {
      PeopleService.allPeople().map(Ok)
    }

  def person: Endpoint[Person] =
    get("v1" :: "people" :: int("id")) { (id: Int) =>
      PeopleService.personDetails(PersonId(id)).map {
        case Some(person) => Ok(person)
        case None => NotFound(notFoundError(s"No person for ID $id"))
      }
    }
}
