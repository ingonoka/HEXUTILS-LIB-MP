@file:Suppress("UnstableApiUsage")

import com.ingonoka.gradle.ArtifactoryConfig
import com.ingonoka.gradle.BuildConfig
import com.ingonoka.gradle.PomConfig
import groovy.lang.GroovyObject
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.gradle.SourceRoot
import org.jfrog.gradle.plugin.artifactory.dsl.PublisherConfig
import org.jfrog.gradle.plugin.artifactory.dsl.ResolverConfig

repositories {
    google()
    jcenter()
    mavenCentral()
}

plugins {

    id("com.android.library")
    kotlin("multiplatform")
    id("org.jetbrains.dokka-android") version "0.9.18"
    id("maven-publish")
    id("com.jfrog.artifactory") version "4.9.1"
}

version = BuildConfig.versionName()
group = BuildConfig.group


android {
    compileSdkVersion(28)
    defaultConfig {
        minSdkVersion(23)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        maybeCreate("release").apply {
            isMinifyEnabled = false
        }
        maybeCreate("debug").apply {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(fileTree("libs") {
        include("*.jar")
    })
}


kotlin {
    android {
        publishLibraryVariants("debug", "release")
    }

    jvm {

    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }


        @Suppress("UNUSED_VARIABLE") val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
            }
        }


        @Suppress("UNUSED_VARIABLE") val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }

        @Suppress("UNUSED_VARIABLE") val androidMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
            }
        }

        @Suppress("UNUSED_VARIABLE") val androidTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
        }

    }
    println("Supported platforms => ${targets.names}")
}


tasks {
    named<DokkaTask>("dokka") {
        outputFormat = "html"
        includes = listOf("src/commonMain/kotlin/com/ingonoka/hexutils/module.md")
        kotlinTasks { listOf() }
        sourceRoots = mutableListOf()

        println("Dokka source directories =>")
        kotlin.sourceSets.forEach { sourceSet ->
            if (!sourceSet.name.endsWith("Test")) {
                sourceSet.kotlin.sourceDirectories.forEach { dir ->
                    if (dir.exists()) {
                        println("    ${sourceSet.name} => $dir")
                        sourceRoots.add(SourceRoot().apply {
                            path = dir.path
                            platforms = when {
                                sourceSet.name.startsWith("jvm") -> listOf("JVM")
                                sourceSet.name.startsWith("common") -> listOf("JVM", "ANDROID")
                                sourceSet.name.startsWith("android") -> listOf("ANDROID")
                                else -> listOf()
                            }
                        })
                    }
                }
            }
        }
    }

    create<Jar>("dokkaJar") {
        group = JavaBasePlugin.DOCUMENTATION_GROUP
        description = "Assembles Kotlin docs with Dokka"
        archiveClassifier.set("javadoc")
        from(dokka)
    }
}

val developerName: String by project

kotlin.targets.forEach {
    it.mavenPublication {
        artifact(tasks["dokkaJar"])
        pom {
            developers {
                developer {
                    name.set(PomConfig.developer)
                }
            }
            licenses {
                license {
                    name.set(PomConfig.licenseName)
                    url.set(PomConfig.licenseUrl)
                }
            }
        }

    }
}

artifactory {
    setContextUrl("http://209.97.175.64:8081/artifactory")
    val repoKey = BuildConfig.artifactoryRepoKey

    publish(delegateClosureOf<PublisherConfig> {
        repository(delegateClosureOf<GroovyObject> {

            setProperty("repoKey", repoKey)
            setProperty("username", ArtifactoryConfig.userName)
            setProperty("password", ArtifactoryConfig.password)
            setProperty("maven", true)
        })
        defaults(delegateClosureOf<GroovyObject> {
            invokeMethod("publications", publishing.publications.names.toTypedArray())
            setProperty("publishIvy", false)
        })
    })

    resolve(delegateClosureOf<ResolverConfig> {
        setProperty("repoKey", repoKey)
        setProperty("username", ArtifactoryConfig.userName)
        setProperty("password", ArtifactoryConfig.password)
        setProperty("maven", true)
    })
}