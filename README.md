# ktor-easy-spa
A feature of ktor for setting up single page application like Angular, React and so on

## Setting
### Maven
```xml
<dependency>
  <groupId>work.jeong.murry.ktor.features</groupId>
  <artifactId>ktor-easy-spa</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```
### Gradle
```groovy
dependencies {
    implementation("work.jeong.murry.ktor.features", "ktor-easy-spa", "{latest_version}")
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
        val defaultFile = "index.html"
        apiUrl = "/api"
    }
```
