group = "vision.kodai"
version = "0.1.0"

plugins {
    scala
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.scala-lang:scala-library:2.13.2")
    testImplementation("junit:junit:4.13")
}

application {
    mainClassName = "vision.kodai.game.Main"
}
