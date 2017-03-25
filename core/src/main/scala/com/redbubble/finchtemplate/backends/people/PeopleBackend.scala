package com.redbubble.finchtemplate.backends.people

import com.redbubble.finchtemplate.model.Person
import com.redbubble.finchtemplate.services.people.PersonId
import com.redbubble.finchtemplate.util.config.Environment.env
import com.redbubble.finchtemplate.util.http.FinchTemplateJsonApiClient.client
import com.redbubble.finchtemplate.util.metrics.Metrics._
import com.redbubble.util.http.RelativePath.emptyPath
import com.redbubble.util.http.syntax._
import com.redbubble.util.http.{DownstreamResponse, JsonApiClient, RelativePath}

trait PeopleBackend {
  protected val peopleApiClient: JsonApiClient

  private implicit val decodePeople = PeopleDecoders.peopleDecoder
  private implicit val decodePerson = PeopleDecoders.personDecoder

  final def allPeople(): DownstreamResponse[Seq[Person]] =
    peopleApiClient.get[Seq[Person]](emptyPath).empty

  final def personDetails(personId: PersonId): DownstreamResponse[Option[Person]] =
    peopleApiClient.get[Option[Person]](RelativePath(personId.toString)).empty
}

object PeopleBackend extends PeopleBackend {
  protected override val peopleApiClient = client(env.peopleApiUrl)(clientMetrics)
}

