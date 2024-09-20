/*
 * Copyright (c) 2024. Ingo Noka
 * This file belongs to project hexutils-mp.
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit https://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 *
 */


import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.mavenPublish)
    alias(libs.plugins.dokkaGradlePlugin)
}

android {
    namespace = "com.ingonoka.hexutils"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    version = project.version

    compileOptions {
        targetCompatibility = JavaVersion.VERSION_21
        sourceCompatibility = JavaVersion.VERSION_21
    }

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-android-optimize.txt")
            testProguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-android-optimize.txt")
        }
        debug {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(libs.junit.jupiter)
}

kotlin {

    jvm().compilations.all {
        compileTaskProvider.configure{
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_21)
            }
        }
    }

    macosX64()

    androidTarget {
        publishLibraryVariants("release", "debug")
    }.compilations.all {
        compileTaskProvider.configure{
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_21)
            }
        }
    }

    sourceSets {
        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
        jvmTest {
        }
        androidUnitTest {
        }
    }
}

val pomDeveloper = "Ingo Noka"
val pomLicenseName = "Attribution-NonCommercial-NoDerivatives 4.0 International (CC BY-NC-ND 4.0)"
val pomLicenseUrl = "https://creativecommons.org/licenses/by-nc-nd/4.0/"

publishing {
    publications.withType<MavenPublication>().configureEach {
        pom {
            name = "HEXUTILS Library"
            description = "A library for converting byte and int arrays to hex strings and vice versa."
            licenses {
                license {
                    name = pomLicenseName
                    url = pomLicenseUrl
                }
            }
            developers {
                developer {
                    name = pomDeveloper
                }
            }
        }
    }
}

tasks.dokkaHtml.configure {
    outputDirectory.set(layout.buildDirectory.get().dir("dokka"))
    moduleName.set("hexutils")

    dokkaSourceSets {
        named("jvmMain") {
            displayName = "JVM"
            platform.set(org.jetbrains.dokka.Platform.jvm)
        }

        named("androidMain") {
            displayName = "Android"
        }

        named("commonMain") {
            platform.set(org.jetbrains.dokka.Platform.common)
            displayName = "Common"
        }

    }
}

repositories {
    mavenCentral()
    google()
}
