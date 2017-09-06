package com.redbubble.finchtemplate.api.v1.people

import com.redbubble.finchtemplate.model.Person
import com.redbubble.util.http.ResponseOps.dataJsonEncode
import io.circe.{Encoder, Json}
import io.finch.Encode

trait PeopleEncoders {
  final val personEncoder: Encoder[Person] = Encoder.instance[Person](p => Json.obj("person" -> personJson(p)))

  final val peopleEncoder: Encoder[Seq[Person]] = Encoder.instance[Seq[Person]] { ps =>
    val peopleJson = ps.map(personJson)
    Json.obj("people" -> Json.arr(peopleJson: _*))
  }

  implicit final val personResponseEncode: Encode.Json[Person] = dataJsonEncode[Person](personEncoder)
  implicit final val peopleResponseEncode: Encode.Json[Seq[Person]] = dataJsonEncode[Seq[Person]](peopleEncoder)

  private def personJson(p: Person) =
    Json.obj(
      "name" -> Json.fromString(p.name),
      "birth_year" -> Json.fromString(p.birthYear),
      "hair_colour" -> Json.fromString(p.hairColour)
    )
}

object PeopleEncoders extends PeopleEncoders
