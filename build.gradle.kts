import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "2.1.21"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    application
}

group = "tech.bufallo.pw.scz"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("tech.bufallo.pw.scz.MainKt")
}

repositories {
    mavenCentral()
}

val clicktVersion = "5.0.3"

dependencies {
    implementation("com.github.ajalt.clikt:clikt:$clicktVersion")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveFileName.set("crc-calculator.jar")
        mergeServiceFiles()
    }

    build {
        dependsOn(shadowJar)
    }
}