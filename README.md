# Overview

The model-data project provides classes for modeling objects and model binding. 
The model-data project is ispired from model pattern of the [wicket project](https://wicket.apache.org/) and most classes have been forked and modified. 

Lombok is used for not generating poilerplate source code.

## License

The source code comes under the liberal Apache License V2.0, making model-data great for all types of applications.

# Build Status 
[![Build Status](https://travis-ci.org/lightblueseas/model-data.svg?branch=master)](https://travis-ci.org/lightblueseas/model-data)

## Maven Central

model-api [![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.alpharogroup/model-api/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.alpharogroup/model-api)

model-object [![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.alpharogroup/model-object/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.alpharogroup/model-object)

model-type-safe [![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.alpharogroup/model-type-safe/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.alpharogroup/model-type-safe)

## javadoc

model-api [![Javadocs](http://www.javadoc.io/badge/de.alpharogroup/model-api.svg)](http://www.javadoc.io/doc/de.alpharogroup/model-api)

model-object [![Javadocs](http://www.javadoc.io/badge/de.alpharogroup/model-object.svg)](http://www.javadoc.io/doc/de.alpharogroup/model-object)

model-type-safe [![Javadocs](http://www.javadoc.io/badge/de.alpharogroup/model-type-safe.svg)](http://www.javadoc.io/doc/de.alpharogroup/model-type-safe)

## Maven dependency

Maven dependency is now on sonatype.
Check out [sonatype repository](https://oss.sonatype.org/index.html#nexus-search;quick~model-data) for latest snapshots and releases.

You can add the following maven dependencies to your project `pom.xml` if you want to import the library. 

You can first define the version properties:

	<properties>
		...
		<!-- MODEL-DATA version -->
		<model-data.version>1.6.2</model-data.version>
		<model-api.version>${model-data.version}</model-api.version>
		<model-core.version>${model-data.version}</model-core.version>
		<model-object.version>${model-data.version}</model-object.version>
		<model-type-safe.version>${model-data.version}</model-type-safe.version>
		...
	</properties>

Add the following maven dependency to your project `pom.xml` if you want to import the core functionality of model-api:

		<dependencies>
			...
			<dependency>
				<groupId>de.alpharogroup</groupId>
				<artifactId>model-api</artifactId>
				<version>${model-api.version}</version>
			</dependency>
			...			
		</dependencies>



Add the following maven dependency to your project `pom.xml` if you want to import the core functionality of model-core:

		<dependencies>
			...
			<dependency>
				<groupId>de.alpharogroup</groupId>
				<artifactId>model-core</artifactId>
				<version>${model-core.version}</version>
			</dependency>
			...			
		</dependencies>


Add the following maven dependency to your project `pom.xml` if you want to import the core functionality of model-object:

		<dependencies>
			...
			<dependency>
				<groupId>de.alpharogroup</groupId>
				<artifactId>model-object</artifactId>
				<version>${model-object.version}</version>
			</dependency>
			...			
		</dependencies>


Add the following maven dependency to your project `pom.xml` if you want to import the core functionality of model-type-safe:

		<dependencies>
			...
			<dependency>
				<groupId>de.alpharogroup</groupId>
				<artifactId>model-type-safe</artifactId>
				<version>${model-type-safe.version}</version>
			</dependency>
			...			
		</dependencies>


## Want to Help and improve it? ###

The source code for model-data are on GitHub. Please feel free to fork and send pull requests!

Create your own fork of [lightblueseas/model-data/fork](https://github.com/lightblueseas/model-data/fork)

To share your changes, [submit a pull request](https://github.com/lightblueseas/model-data/pull/new/develop).

Don't forget to add new units tests on your changes.

## Contacting the Developers

Do not hesitate to contact the model-data developers with your questions, concerns, comments, bug reports, or feature requests.
- Feature requests, questions and bug reports can be reported at the [issues page](https://github.com/lightblueseas/model-data/issues).

## Note

No animals were harmed in the making of this library.

# Donate

If you like this library, please consider a donation through 
<a href="https://flattr.com/submit/auto?fid=r7vp62&url=https%3A%2F%2Fgithub.com%2Flightblueseas%2Fmodel-data" target="_blank">
<img src="http://button.flattr.com/flattr-badge-large.png" alt="Flattr this" title="Flattr this" border="0">
</a>
