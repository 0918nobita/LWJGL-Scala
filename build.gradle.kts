plugins {
    scala
    application
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("junit:junit:4.13")
}

application {
    mainClassName = "game.App"
}
