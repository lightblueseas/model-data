## Change log
----------------------

Version 3.2-SNAPSHOT
-------------

ADDED:

- new Makefile with build, test, release and publish targets
- new license header file src/main/resources/license-header.txt for the spotless licenseHeaderFile step
- new gradle file tagging.gradle with the tagRelease task based on a plain git Exec task
- new publishing repository configuration for the Central Portal (releases over the OSSRH staging API, snapshots to central.sonatype.com) with credentials from CENTRAL_USERNAME/CENTRAL_PASSWORD or the gradle properties centralUsername/centralPassword
- new github-actions workflow publish.yml that publishes to Maven Central on RELEASE-* tags with in-memory GPG signing
- new gradle plugin org.gradle.toolchains.foojay-resolver-convention in version 1.0.0 for automatic JDK provisioning
- new apply-gradle-files mechanism with gradle/gradle-files.list

CHANGED:

- update gradle to new version 9.7.0
- update of gradle plugin io.freefair.lombok to new version 9.5.0
- update of gradle plugin com.diffplug.spotless to new version 8.10.0
- update of gradle plugin nl.littlerobots.version-catalog-update to new version 1.1.1
- update of gradle plugin ben-manes versions to new version 0.61.0 with new plugin id io.github.ben-manes.versions
- update of dependency commons-lang3 to new version 3.20.0
- update of dependency lombok to new version 1.18.46
- update of test dependency junit-jupiter to new major version 6.1.3
- update of test dependency randomizer to new version 10.3
- removed the grgit gradle plugin; the tagRelease task now uses a plain git Exec task, so the gradle configuration cache works without workarounds
- removed the license gradle plugin; license headers are now managed by the spotless licenseHeaderFile step
- removed obsolete test dependencies hamcrest-all and junit-jupiter-params; the affected tests now use the JUnit Jupiter assertions
- github-actions workflow: removed obsolete ossrh secrets, updated setup-gradle to v4 and codecov-action to v5
- corrected the license information in gradle.properties from MIT to Apache License, Version 2.0 so the published pom matches the LICENSE file and the source headers
- module-info: lombok is now a static (compile-time only) requirement and the redundant java.base requirement was removed
- enabled the gradle configuration cache

Version 3.1
-------------

- see git history for previous releases
