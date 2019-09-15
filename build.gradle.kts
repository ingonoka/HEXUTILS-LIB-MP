

buildscript {

    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.5.0")
        classpath(kotlin("gradle-plugin", version = embeddedKotlinVersion))
    }
}
repositories {
    google()
    jcenter()
}
