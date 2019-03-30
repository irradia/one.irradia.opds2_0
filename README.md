one.irradia.opds2_0
===

[![Build Status](https://img.shields.io/travis/irradia/one.irradia.opds2_0.svg?style=flat-square)](https://travis-ci.org/irradia/one.irradia.opds2_0)
[![Maven Central](https://img.shields.io/maven-central/v/one.irradia.opds2_0/one.irradia.opds2_0.api.svg?style=flat-square)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22one.irradia.opds2_0%22)
[![Maven Central (snapshot)](https://img.shields.io/nexus/s/https/oss.sonatype.org/one.irradia.opds2_0/one.irradia.opds2_0.api.svg?style=flat-square)](https://oss.sonatype.org/content/repositories/snapshots/one.irradia.opds2_0/)
[![Codacy Badge](https://img.shields.io/codacy/grade/905e3715c5f94162872dcfaa3ffd1316.svg?style=flat-square)](https://www.codacy.com/app/github_79/one.irradia.opds2_0?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=irradia/one.irradia.opds2_0&amp;utm_campaign=Badge_Grade)
[![Codecov](https://img.shields.io/codecov/c/github/irradia/one.irradia.opds2_0.svg?style=flat-square)](https://codecov.io/gh/irradia/one.irradia.opds2_0)
[![Gitter](https://badges.gitter.im/irradia-org/community.svg)](https://gitter.im/irradia-org/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

![opds2_0](./src/site/resources/opds2_0.jpg?raw=true)

## Features

* Efficient and strict parsing of [OPDS 2.0](https://specs.opds.io/opds-2.0.html) feeds
* Extensible parser for consuming extension data inserted into feeds in a type-safe manner
* Explicit support for registering parser extensions via [ServiceLoader](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/ServiceLoader.html) 
* Parsed feeds exposed as immutable and persistent data types for correctness and thread-safety
* ISC license
* High coverage automated test suite

## Building

Install the Android SDK.

```
$ ./gradlew clean assembleDebug test
```

If the above fails, it's a bug. Report it!

## Using

Use the following Maven or Gradle dependencies, replacing `${LATEST_VERSION_HERE}` with
whatever is the latest version published to Maven Central:

```
<!-- API -->
<dependency>
  <groupId>one.irradia.opds2_0</groupId>
  <artifactId>one.irradia.opds2_0.api</artifactId>
  <version>${LATEST_VERSION_HERE}</version>
</dependency>

<!-- Parser API -->
<dependency>
  <groupId>one.irradia.opds2_0</groupId>
  <artifactId>one.irradia.opds2_0.parser.api</artifactId>
  <version>${LATEST_VERSION_HERE}</version>
</dependency>

<!-- Default implementation -->
<dependency>
  <groupId>one.irradia.opds2_0</groupId>
  <artifactId>one.irradia.opds2_0.parser.vanilla</artifactId>
  <version>${LATEST_VERSION_HERE}</version>
</dependency>
```

```
repositories {
  mavenCentral()
}

implementation "one.irradia.opds2_0:one.irradia.opds2_0.api:${LATEST_VERSION_HERE}"
implementation "one.irradia.opds2_0:one.irradia.opds2_0.parser.api:${LATEST_VERSION_HERE}"
implementation "one.irradia.opds2_0:one.irradia.opds2_0.parser.vanilla:${LATEST_VERSION_HERE}"
```

Library code is encouraged to depend only upon the API package in order to give consumers
the freedom to use other implementations of the API if desired.

## Modules

|Module|Description|
|------|-----------|
| [one.irradia.opds2_0.api](https://github.com/irradia/one.irradia.opds2_0/tree/develop/one.irradia.opds2_0.api) | Core API
| [one.irradia.opds2_0.lexical](https://github.com/irradia/one.irradia.opds2_0/tree/develop/one.irradia.opds2_0.lexical) | Lexical types used by parsers
| [one.irradia.opds2_0.parser.api](https://github.com/irradia/one.irradia.opds2_0/tree/develop/one.irradia.opds2_0.parser.api) | Parser API
| [one.irradia.opds2_0.parser.extension.spi](https://github.com/irradia/one.irradia.opds2_0/tree/develop/one.irradia.opds2_0.parser.extension.spi) | Parser extension SPI
| [one.irradia.opds2_0.tests.device](https://github.com/irradia/one.irradia.opds2_0/tree/develop/one.irradia.opds2_0.tests.device) | Unit tests that execute on real or emulated devices
| [one.irradia.opds2_0.tests](https://github.com/irradia/one.irradia.opds2_0/tree/develop/one.irradia.opds2_0.tests) | Unit tests that can execute without needing a real or emulated device
| [one.irradia.opds2_0.vanilla](https://github.com/irradia/one.irradia.opds2_0/tree/develop/one.irradia.opds2_0.vanilla) | Vanilla parser implementation

## Publishing Releases

Releases are published to Maven Central with the following invocation:

```
$ ./gradlew clean assembleDebug publish closeAndReleaseRepository
```

Consult the documentation for the [Gradle Signing plugin](https://docs.gradle.org/current/userguide/signing_plugin.html)
and the [Gradle Nexus staging plugin](https://github.com/Codearte/gradle-nexus-staging-plugin/) for
details on what needs to go into your `~/.gradle/gradle.properties` file to do the appropriate
PGP signing of artifacts and uploads to Maven Central.

## Semantic Versioning

All [irradia.one](https://www.irradia.one) packages obey [Semantic Versioning](https://www.semver.org)
once they reach version `1.0.0`.
