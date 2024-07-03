# Step-by-Step Guide to Migrate from TestNG to JUnit-Jupiter

## 1. Update Dependencies


First, remove the TestNG dependency and add the required JUnit-Jupiter dependencies. Update your `libs.versions.toml` 
file to look like the following:

```toml
[versions]
junit-jupiter-params-version = "5.11.0-M2"
junit-jupiter-version = "5.11.0-M2"
junit-platform-launcher-version = "1.11.0-M2"

[libraries]
junit-jupiter = { module = "org.junit.jupiter:junit-jupiter", version.ref = "junit-jupiter-version" }
junit-jupiter-params = { module = "org.junit.jupiter:junit-jupiter-params", version.ref = "junit-jupiter-params-version" }
junit-platform-launcher = { module = "org.junit.platform:junit-platform-launcher", version.ref = "junit-platform-launcher-version" }

[bundles]
unit-testing = [
   "junit-jupiter",
   "junit-jupiter-params",
]
```
Next, update the dependencies section in your build script:
```groovy
dependencies {
    // Note: use of bundles...
    testImplementation libs.bundles.unit.testing
    testRuntimeOnly libs.junit.platform.launcher
}
```
Also, modify the test section to use the JUnit platform:
```groovy
test {
    useJUnitPlatform()
}
```
## 2. Replace TestNG Imports
   
Replace all TestNG imports:

```java
import org.testng.annotations.Test;
```
with this one:
```java
import org.junit.jupiter.api.Test;
```

Now replace all static method imports from testng

```java
import static org.testng.AssertJUnit.*;
```
and
```java
import static org.testng.Assert.*;
```
with this one:
```java
import static org.junit.jupiter.api.Assertions.*;
```

# Replace TestNG expectedExceptions

Replace TestNG's expectedExceptions with the corresponding JUnit-Jupiter construct:

From
```java
 @Test(expectedExceptions = UnsupportedOperationException.class)
 public void targetReadOnly()
 {
    // ...
 }
```
to
```java

@Test
public void targetReadOnly()
{
   Assertions.assertThrows(UnsupportedOperationException.class, () -> {
      // ...
   });
}
```
after that you can replace all assertEquals from
From
```java
@Test
public void testMethod()
{
   assertEquals(actual, expected);
}

```
to
```java
@Test
public void testMethod()
{
   assertEquals(expected, actual);
}
```
