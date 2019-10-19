import org.apache.commons.io.output.ByteArrayOutputStream
import org.gradle.kotlin.dsl.`kotlin-dsl`

plugins {
    `kotlin-dsl`
    id("java")
}

buildscript {

    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.5.1")
        classpath(kotlin("gradle-plugin", version = embeddedKotlinVersion))
    }
}

repositories {
    jcenter()
}


tasks {
    create<Exec>("updateVersions") {

        standardOutput = ByteArrayOutputStream()
        commandLine = listOf("/usr/local/bin/bash", "get-gradle-versions.sh")
        val myFile = File("src/main/java/com/ingonoka/gradle/LibraryVersions.kt")
        doLast {
            if(myFile.exists())
                myFile.renameTo(File("src/main/java/com/ingonoka/gradle/LibraryVersions.kt.bak"))

            myFile.createNewFile()
            myFile.writeBytes(standardOutput.toString().toByteArray(Charsets.UTF_8))
        }
    }
}