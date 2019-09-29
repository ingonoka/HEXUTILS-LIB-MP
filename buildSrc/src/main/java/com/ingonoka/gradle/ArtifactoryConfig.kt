package com.ingonoka.gradle

object ArtifactoryConfig {

    @JvmStatic
    val userName = "ingonoka"

    @JvmStatic
    val password = "AP36i5WZAqnFjDdzAs3dmNDSYhEpUterfjmopx"

    @JvmStatic
    fun describe(): String {
        return """
            |Artifactory Configuration:
            |   User Name:             $userName
        """.trimMargin()
    }
}