package com.redbubble.finchtemplate.util.spec

import com.redbubble.util.spec.gen.{GenHelpers, JsonGenerators, StdLibGenerators}

trait FinchTemplateGenerators extends StdLibGenerators
    with JsonGenerators
    with GenHelpers
    with PeopleGenerators

object FinchTemplateGenerators extends FinchTemplateGenerators
