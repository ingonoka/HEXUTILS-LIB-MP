@file:Suppress("UnstableApiUsage")

import com.ingonoka.gradle.ArtifactoryConfig
import com.ingonoka.gradle.BuildConfig
import com.ingonoka.gradle.PomConfig
import groovy.lang.GroovyObject
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.gradle.SourceRoot
import org.jfrog.gradle.plugin.artifactory.dsl.PublisherConfig
import org.jfrog.gradle.plugin.artifactory.dsl.ResolverConfig

println(BuildConfig.describe())
println(ArtifactoryConfig.describe())
println(PomConfig.describe())

repositories {
    google()
    jcenter()
    mavenCentral()
}

plugins {

    id("com.android.library")
    kotlin("multiplatform")
    id("org.jetbrains.dokka") version "0.9.18"
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
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        maybeCreate("release").apply {
            isMinifyEnabled = false
        }
        maybeCreate("debug").apply {
            isMinifyEnabled = false
        }
    }

    sourceSets {
        @Suppress("UNUSED_VARIABLE") val main by getting {
            manifest.srcFile("src/main/AndroidManifest.xml")
            java.srcDirs("src/main/kotlin")
            res.srcDirs("src/main/res")
            resources.srcDir("src/main/resources")

        }
    }

    sourceSets {
        @Suppress("UNUSED_VARIABLE") val androidTest by getting {
            manifest.srcFile("src/main/AndroidManifest.xml")
            java.srcDirs("src/androidTest/kotlin")
            res.srcDirs("src/androidTest/res")
            resources.srcDir("src/androidTest/resources")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packagingOptions {
        exclude("META-INF/atomicfu.kotlin_module")
        exclude("META-INF/kotlinx-io.kotlin_module")
    }
}

dependencies {
    implementation(fileTree("libs") {
        include("*.jar")
    })


    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("junit:junit:4.12")
    androidTestImplementation("org.jetbrains.kotlin:kotlin-test")
    androidTestImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.1")

    implementation("org.jetbrains.kotlinx:atomicfu:0.13.0")
    implementation("org.jetbrains.kotlinx:kotlinx-io-jvm:0.1.14")


}


kotlin {
    android("android") {
        publishLibraryVariants("debug", "release")
    }

    jvm {
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-io:0.1.14")

                implementation("org.jetbrains.kotlinx:atomicfu-common:0.13.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.3.1")

            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.1")
            }
        }


        @Suppress("UNUSED_VARIABLE") val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation("org.jetbrains.kotlinx:kotlinx-io-jvm:0.1.14")

                implementation("org.jetbrains.kotlinx:atomicfu:0.13.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.1")


            }
        }


        @Suppress("UNUSED_VARIABLE") val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.1")


            }
        }

        @Suppress("UNUSED_VARIABLE") val androidMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation("org.jetbrains.kotlinx:kotlinx-io:0.1.14")

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.1")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.1")


            }
        }

        @Suppress("UNUSED_VARIABLE") val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.1")

            }
        }
    }
    println(
        """
            |Supported platforms:
            |   ${targets.names}
        """.trimMargin()
    )
}


fun printTargetsAndCompilations() {
    println("Kotlin Targets:")
    kotlin.targets.forEach {
        println("  ${it.name}")
        println("    Compilations:")
        it.compilations.forEach { compilation ->
            println("      ${compilation.name}")
            println("        Source Sets:")
            compilation.allKotlinSourceSets.forEach { sourceSet ->
                println("          ${sourceSet.name}:")
                println("            Paths:")
                sourceSet.kotlin.sourceDirectories.forEach { dir ->
                    println("              ${dir}")
                }
            }
        }
    }
    println("Android Sourcesets:")
    android.sourceSets.forEach {
        println("  ${it.name}")
        println("    ${it.manifest}")
        println("    ${it.res}")
        println("      Sources")
        it.java.srcDirs.forEach {
            println("      $it")
        }
        println("      Resources:")
        it.resources.srcDirs.forEach {
            println("      $it")
        }
    }
}

tasks {

    named("build") {
        dependsOn.remove("check")
    }

    named<DokkaTask>("dokka") {
        outputFormat = "html"
        includes = listOf("src/commonMain/kotlin/com/ingonoka/hexutils/module.md")
        kotlinTasks { listOf() }
        sourceRoots = mutableListOf()

        kotlin.sourceSets.forEach { sourceSet ->
            if (!sourceSet.name.endsWith("Test")) {
                sourceSet.kotlin.sourceDirectories.forEach { dir ->
                    if (dir.exists()) {
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

    create("printTargets") {
        doLast {
            printTargetsAndCompilations()
        }
    }
}

afterEvaluate {
//    printTargetsAndCompilations()
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

// This is for publishing to Github - does not work yet, but authentication with personal access token seems ok
//publishing {
//    repositories {
//        maven {
//            url = uri("https://maven.pkg.github.com/ingonoka/BERTLV-LIB-MP")
//            name = "GitHubPackages"
//            credentials {
//                username = "ingonoka"
//                @Suppress("SpellCheckingInspection")
//                password = "b5c6a065568f3fbcff1d61c57dd76fbc5a3226f6"
//            }
//        }
//    }
//}

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
