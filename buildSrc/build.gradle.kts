import org.gradle.kotlin.dsl.`kotlin-dsl`

plugins {
    `kotlin-dsl`
}
// Required since Gradle 4.10+.
repositories {
    jcenter()
}
/*
buildscript {

    repositories {
        jcenter()
        google()
    }

    dependencies {
        // Kotlin
        classpath(kotlin("gradle-plugin", version = "1.3.50"))
    }
}

repositories {
    jcenter()
    google()
}

plugins {
  kotlin("kotlin-jvm")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
} */
