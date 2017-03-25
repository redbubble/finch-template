package com.redbubble.finchtemplate.model

import com.redbubble.finchtemplate.model.people.{BirthYear, HairColour, Name}

final case class Person(name: Name, birthYear: BirthYear, hairColour: HairColour)
