package com.redbubble.finchtemplate.fetch.people

import cats.data.NonEmptyList
import com.redbubble.finchtemplate.backends.people.PeopleBackend
import com.redbubble.finchtemplate.fetch.DataSourceOps._
import com.redbubble.finchtemplate.model.Person
import fetch._

object PeopleDataSource extends DataSource[Unit, Seq[Person]] {
  override def name = "People"

  override def identity(i: Unit) = (name, "AllPeople")

  override def fetchOne(id: Unit) = asyncQuery(PeopleBackend.allPeople())

  override def fetchMany(ids: NonEmptyList[Unit]) = batchingNotSupported(ids)
}
