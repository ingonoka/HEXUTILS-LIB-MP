package com.ingonoka.gradle

object PomConfig {

    @JvmStatic
    val developer = "Ingo Noka"

    @JvmStatic
    val licenseName = "Attribution-NonCommercial-NoDerivatives 4.0 International (CC BY-NC-ND 4.0)"

    @JvmStatic
    val licenseUrl = "https://creativecommons.org/licenses/by-nc-nd/4.0/"


    @JvmStatic
    fun describe(): String {
        return """
            |Pom Configuration:
            |   Developer:             $developer
            |   License:               $licenseName
            |   License URL:           $licenseUrl
        """.trimMargin()
    }
}