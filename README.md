# FTCScript
Rapidly edit, test, and run OpModes through the use of Kotlin scripts.

> [!WARNING]  
> FTC script is not yet documented and is not yet production-ready.

## Documentation:
- [How it works](https://github.com/GrowlyX/ftc-scripting/blob/master/docs/how-it-works.md)
- [Contributing to this project](https://github.com/GrowlyX/ftc-scripting/blob/master/docs/developers.md)
- [Setting up FTCScript locally](https://github.com/GrowlyX/ftc-scripting/blob/master/docs/configure.md)

## Getting started:
Current Version: `1.0.2-SNAPSHOT`
### Maven:
```xml
<repositories>
    <repository>
        <id>artifactory.scala.gg</id>
        <url>https://artifactory.scala.gg/artifactory/opensource</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>io.liftgate.ftc.scripting</groupId>
        <artifactId>[module]</artifactId> 
        <version>[version]</version>
    </dependency>
</dependencies>
```

### Gradle:
Groovy:
```groovy
repositories {
    maven {
        url = "https://artifactory.scala.gg/artifactory/opensource"
    }
}

dependencies {
    implementation("io.liftgate.ftc.scripting:[module]:[version]")
}
```
Kotlin:
```kotlin
repositories {
    maven(url = "https://artifactory.scala.gg/artifactory/opensource")
}

dependencies {
    implementation("io.liftgate.ftc.scripting:[module]:[version]")
}
```
