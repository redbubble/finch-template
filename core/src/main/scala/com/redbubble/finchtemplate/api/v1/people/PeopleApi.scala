package com.redbubble.finchtemplate.api.v1.people

import com.redbubble.finchtemplate.model.Person
import com.redbubble.finchtemplate.model.people.{BirthYear, HairColour, Name}
import io.finch.{Endpoint, _}

object PeopleApi {
  val peopleApi = people :+: person

  def people: Endpoint[Seq[Person]] =
    get("v1" :: "pepople") {
      Ok(Seq(Person(Name("Bobba"), BirthYear("BY17"), HairColour("Helmet"))))
    }

  def person: Endpoint[Person] =
    get("v1" :: "people" :: int("id")) { (id: Int) =>
      Ok(Person(Name("Bobba"), BirthYear("BY17"), HairColour("Helmet")))
    }
}
