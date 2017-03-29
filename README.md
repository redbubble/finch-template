# Finch HTTP Service Template

This is a template for a simple, stateless HTTP API built on top of Finch. It aims to provide a simple, consistent, beginner- to intermediate-level stack, aimed at getting a small HTTP-based service up & running quickly with some things we care about in a production system, including:

* A HTTP API;
* Authentication;
* Caching;
* System monitoring;
* Error reporting;
* Metrics;
* Logging;
* Testing;
* Deployment.

This template uses other open source code from Redbubble:

* [finagle-hawk](https://github.com/redbubble/finagle-hawk) - HTTP Holder-Of-Key Authentication Scheme for Finagle.

Redbubble also makes available a [GraphQL version of this template](https://github.com/redbubble/rb-graphql-template) (which is more complete and contains a Docker image).

If you're after a basic Finch template, several are available:

* [finch-quickstart](https://github.com/zdavep/finch-quickstart/);
* [finch-server](https://github.com/BenWhitehead/finch-server)

With more are in the [cookbook](http://finagle.github.io/finch/best-practices.html#pulling-it-all-together).

# API

You can access the API as follows.

## People

```
$ curl -i "http://localhost:8080/v1/people"
HTTP/1.1 200 OK
Content-Type: application/json
Date: Mon, 27 Mar 2017 10:19:46 GMT
Content-Length: 692

{"data":{"people":[{"name":"Luke Skywalker","birth_year":"19BBY","hair_colour":"blond"},{"name":"C-3PO","birth_year":"112BBY","hair_colour":"n/a"},{"name":"R2-D2","birth_year":"33BBY","hair_colour":"n/a"},{"name":"Darth Vader","birth_year":"41.9BBY","hair_colour":"none"},{"name":"Leia Organa","birth_year":"19BBY","hair_colour":"brown"},{"name":"Owen Lars","birth_year":"52BBY","hair_colour":"brown, grey"},{"name":"Beru Whitesun lars","birth_year":"47BBY","hair_colour":"brown"},{"name":"R5-D4","birth_year":"unknown","hair_colour":"n/a"},{"name":"Biggs Darklighter","birth_year":"24BBY","hair_colour":"black"},{"name":"Obi-Wan Kenobi","birth_year":"57BBY","hair_colour":"auburn, white"}]}}
```

## Person

```
$ curl -i "http://localhost:8080/v1/people/1"
HTTP/1.1 200 OK
Content-Type: application/json
Date: Mon, 27 Mar 2017 10:20:40 GMT
Content-Length: 88

{"data":{"person":{"name":"Luke Skywalker","birth_year":"19BBY","hair_colour":"blond"}}}
```

## Healthcheck

```
$ curl -i "http://localhost:8080/v1/health"
HTTP/1.1 200 OK
Content-Length: 2
Content-Language: en
Content-Type: text/plain
Date: Mon, 27 Mar 2017 09:46:17 GMT

OK
```

# Setup

To setup for local development, run these steps. Note that most steps assume that you've changed directory to `app`.

1. Install [Java 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) from Oracle.
   You will need a JDK (not a JRE), as of the time of writing this is "Java SE Development Kit 8u92". There is also
   [documentation](http://www.oracle.com/technetwork/java/javase/documentation/jdk8-doc-downloads-2133158.html)
   available (handy for linking into your IDE).

1. Run [sbt](http://www.scala-sbt.org/release/docs/Getting-Started/Setup.html):

    ```
    $ cd app
    $ ./sbt
    > update
    ```

    Note. You could also `brew install sbt` if you'd prefer a system version.

1. `etc/development-env` contains a template `.env` file that you can use to configure your environment variables locally:

    ```
    $ cd app
    $ cp etc/development.env .env
    ```

  See [sbt-dotenv](https://github.com/mefellows/sbt-dotenv) for more information. *Do not check `.env` into source control!*.

  Note that if you change this while an sbt session is running, you will need to `reload` sbt for the new settings to
  take effect (as it's an sbt plugin).

1. If you're using IntelliJ for development, grab the Scala plugin, and import the project (File->Open, or Import from
   the welcome screen) as an SBT project. If you want to run tests, you will need to add the environment variables (in
   `.env`) to the run configurations (specs2).

# Running

## Locally

To run using sbt:

```
$ ./sbt run
```

You can also use [Revolver](https://github.com/spray/sbt-revolver) for restarts when code changes (recommended!):

```
$ ./sbt ~re-start
```

To run using the Heroku tools (requires deployment setup as above), i.e. like it's run in production:

```
$ ./sbt compile stage
$ heroku local        # which uses [forego](https://github.com/ddollar/forego)
```

To run in debug mode:

```
$ ./sbt run -jvm-debug 5005
```

You can then connect a remote debugger to the JVM.

## Production

Deploying to production will restart the app servers (Heroku Dynos) automatically. However if you wish to restart
manually you can:

```
$ heroku restart -a rb-graphql-template
```

# Testing

```
$ ./sbt test
```

This will start the `sbt` REPL, from where you can issue [commands](http://www.scala-sbt.org/0.13/docs/Running.html#Common+commands).

* `test` - Runs all tests;
* `test-only com.redbubble.gql.core.CurrencySpec` - Runs a single test;
* `test-only com.redbubble.gql.core.*` - Runs all tests in the `com.redbubble.gql.core` package;
* `test-only *CurrencySpec` - Runs `CurrencySpec`;
* `test:compile` - Compiles your test code.

Appending a `~` to the start of any sbt command will run it continuously; for example to run tests continuously:

```
> ~test
```

# Performance Testing

*Note. There are incompatibilities with Gatling & the Netty that Finagle uses at present, the performance tests may not work. If you want to use them, the recommendation is to move back down to Scala 2.11 (for all deps) and use that Gatling version.*

Performance testing uses [Gattling](http://gatling.io/), and live in the `perf/src/it` directory. You can run
all performance tests using the following:

```
> gatling-it:test
```

Or individual scenarios as:

```
> gatling-it:test-only com.redbubble.perf.scenarios.AppStartup
> gatling-it:test-only *AppStartup
```

For more information see: http://gatling.io/docs/2.2.4/extensions/sbt_plugin.html

Note. If you run tests against a local server you will need to start it first.

# Deployment

Deployment is to Heroku, which can be done using:

```
$ ./deploy
```

Before you do though, there is a one-off setup for deployment.

1. Get an account on Heroku.

1. Install [heroku toolbelt](https://toolbelt.heroku.com/).

1. Add the git remote (this is a one-off step).

    ```
    $ heroku git:remote -a finch-template
    ```

The deployment uses the [SBT Native Packager](https://github.com/sbt/sbt-native-packager) to package up the artifacts
for deployment. You can run this locally using the `stage` command:

```
$ ./sbt compile stage
```
Here is more information on deployment:

* [Scala on Heroku](https://devcenter.heroku.com/articles/deploying-scala)
* [Scala Buildpack](https://github.com/heroku/heroku-buildpack-scala)
* [An sbt plugin for deploying Heroku Scala applications](https://github.com/heroku/sbt-heroku)

# Logs

## Local

All logs go to standard out locally when developing.

## Production

You can get the logs using:

```
$ heroku logs -t -a finch-template
```

This will only store the last 1500 lines, if you want to view more you can enable the PaperTrail plugin.

# Monitoring

The system is monitored via New Relic. Note that via Heroku we don't get system level metrics such as CPU & memory.

# Metrics

Metrics are exposed via [Twitter Metrics](https://twitter.github.io/finagle/guide/Metrics.html). See
`com.redbubble.gql.util.metrics.Metrics` for the entry point.

## Local

Metrics are available locally on the admin server: http://localhost:9990/admin

## Production

Metrics into production are bridged to New Relic (sent every minute) as custom metrics and are available on the New
Relic insights dashboard.

Note that when sending to New Relic, we do filter out some metrics that are collected locally; in particular the JVM
metrics (as NR collects these already), as well as tools we don't use (e.g. Zipkin). We also only send the 75th, 95th,
99th & 99.9th percentiles, along with 1, 5 & 15 minute weighted moving averages (for the appropriate metrics type where
supported).

# Development Overview

This section is aimed at developers on the project, and gives a quick overview of the features & lbraries used:

* HTTP stack, using [Finch](https://github.com/finagle/finch);
* Authentication using [Hawk](https://github.com/hueniverse/hawk), a HMAC-style protocol;
* JSON encoding & decoding, using [Circe](https://github.com/circe/circe), including reasonable error handling;
* Caching, batching & fetching using [Fetch](http://47deg.github.io/fetch/docs);
* Downstream service clients using [Featherbed](https://finagle.github.io/featherbed);
* [Metrics](https://twitter.github.io/finagle/guide/Metrics.html), logged to New Relic;
* System monitoring via New Relic;
* Error reporting to [Rollbar](https://rollbar.com)
* Testing using [specs2](https://etorreborre.github.io/specs2/) & [ScalaCheck](https://www.scalacheck.org);
* Logging to stdout;
* Deployment packaging using [SBT Native Packager](https://github.com/sbt/sbt-native-packager).

## Tools/Frameworks/Libraries

### Finch

* [Finch best practices](https://github.com/finagle/finch/blob/master/docs/best-practices.md)
* [Finagle 101](http://vkostyukov.net/posts/finagle-101/)
* [Finch 101](http://vkostyukov.ru/slides/finch-101/)

### Finagle

* [Getting started with Finagle](http://andrew-jones.com/blog/getting-started-with-finagle/)
* [An introduction to Finagle](http://twitter.github.io/scala_school/finagle.html)
* [Finagle examples](https://www.codatlas.com/github.com/twitter/finagle/develop)
* [Other information on Finagle](http://dirtysalt.github.io/finagle.html)

### Cats

* [Cats documentation](http://typelevel.org/cats/)
* [Herding Cats](http://eed3si9n.com/herding-cats/) - an introduction/tutorial on Cats

## Uninstall

You can uninstall everything you installed for this project by:

```
$ rm -rf ~/.sbt
$ rm -rf ~/.ivy2
```

Then, if you want, you can uninstall Java by following the instructions here: https://docs.oracle.com/javase/8/docs/technotes/guides/install/mac_jdk.html#A1096903
