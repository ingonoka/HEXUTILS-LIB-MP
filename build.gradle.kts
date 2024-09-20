/*
 * Copyright (c) 2024. Ingo Noka
 * This file belongs to project hexutils-mp.
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit https://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 *
 */

import org.asciidoctor.gradle.jvm.AsciidoctorTask
import java.io.ByteArrayOutputStream

/*
 * Copyright (c) 2021. Ingo Noka
 * This file belongs to project hexutils-mp.
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 *
 */

buildscript{

    repositories {
        google()
        mavenCentral()
    }
}

repositories {
    google()
    mavenCentral()
}

allprojects {
    group = "com.ingonoka"
    version = getVersionName()
}

plugins {
//    id("root.publication")
    //trick: for the same plugin versions in all submodules
    alias(libs.plugins.androidLibrary).apply(false)
    alias(libs.plugins.kotlinMultiplatform).apply(false)
    alias(libs.plugins.asciiDocGradlePlugin)
}

/**
 * Get a version name of the form "v0.3-8-g9518e52", which is the tag
 * assigned to the commit (v0.3), the number of commits since the
 * commit the tag is assigned to and the hash of the latest commit
 */
fun getVersionName(): String = try {
    val stdout = ByteArrayOutputStream()
    exec {
        commandLine = listOf("git", "describe", "--tags") //, '--long'
        standardOutput = stdout
    }
    stdout.toString().trim()
} catch (e: Exception) {
    println(e.message)
    "na"
}

tasks {
    "asciidoctor"(AsciidoctorTask::class) {
        baseDirFollowsSourceDir()
        sourceDir(file("doc"))
        setOutputDir(file("build/docs"))
        asciidoctorj {
            attributes(
                mapOf(
                    "source-highlighter" to "rouge",
                    "library-version" to project.version
                )
            )
        }
    }
}