# ktor-easy-spa
A feature of ktor for setting up single page application like Angular, React and so on

[![Maven Central](https://img.shields.io/maven-central/v/work.jeong.murry.ktor.features/ktor-easy-spa.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22work.jeong.murry.ktor.features%22%20AND%20a:%22ktor-easy-spa%22)

## Setting
### Gradle Kotlin DSL
```groovy
compile("work.jeong.murry.ktor.features:ktor-easy-spa:1.0")
```

### Maven
```xml
<dependency>
  <groupId>work.jeong.murry.ktor.features</groupId>
  <artifactId>ktor-easy-spa</artifactId>
  <version>1.0</version>
</dependency>
```
### Gradle
```groovy
dependencies {
    implementation("work.jeong.murry.ktor.features", "ktor-easy-spa", "1.0")
}
```

## Usage
Just install the feature in your application with:
```kotlin
    install(EasySpaFeature)
```
or
```kotlin
    install(EasySpaFeature) {
        staticRootDocs = "./htdocs"
        defaultFile = "index.html"
        apiUrl = "/api"
    }
```
