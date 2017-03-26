package com.redbubble.finchtemplate.api.v1.people

import com.redbubble.finchtemplate.model.Person
import com.redbubble.util.http.ResponseOps.rootJsonEncode
import io.circe.{Encoder, Json}
import io.finch.Encode

trait PeopleEncoders {
  val personEncoder: Encoder[Person] = Encoder.instance[Person](p => Json.obj("person" -> personJson(p)))

  val peopleEncoder: Encoder[Seq[Person]] = Encoder.instance[Seq[Person]] { ps =>
    val peopleJson = ps.map(personJson)
    Json.obj("people" -> Json.arr(peopleJson: _*))
  }

  implicit val personResultEncode: Encode.Json[Person] = rootJsonEncode[Person](personEncoder)
  implicit val peopleResultEncode: Encode.Json[Seq[Person]] = rootJsonEncode[Seq[Person]](peopleEncoder)

  private def personJson(p: Person) =
    Json.obj(
      "name" -> Json.fromString(p.name),
      "birth_year" -> Json.fromString(p.birthYear),
      "hair_colour" -> Json.fromString(p.hairColour)
    )
}

object PeopleEncoders extends PeopleEncoders
