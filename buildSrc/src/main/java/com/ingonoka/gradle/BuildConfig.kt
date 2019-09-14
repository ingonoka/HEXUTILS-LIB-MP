/*
 * Copyright Ingo Noka.
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.ingonoka.gradle

import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

val versionFormat = "v([0-9]{1,2})\\.([0-9]{1,2}).*".toRegex()


object BuildConfig {

    @JvmStatic
    var isDebug = true

    @JvmStatic
    var isSnapshot = false

    @JvmStatic
    var group: String = "com.ingonoka"

    @JvmStatic
    val artifactoryRepoKey: String
        get() = if (isSnapshot) "libs-snapshot-local" else "libs-release-local"


    /**
     * The short version name has the form "vN.M" with N being the major version (0-99) and M the minor
     * version 0-99.  This only works if the git tags are correctly applied and named.
     */
    @JvmStatic
    val shortVersionName
        get() = runSystemCmd("git describe --abbrev=0")

    /**
     * Get a ong version name that can have two forms:
     * 1. v1.0-1-g5f1ceb7 (version based on commit g5f1ceb7, which is 1 commit away from tag v1.0
     * 2. v1.0 (version is based on commit that is linked to tag v1.0)
     * The suffix "-dirty" is appended if there are changes in the git working tree that have not
     * been committed.
     *
     */
    @JvmStatic
    val longVersionName
        get() = runSystemCmd("git describe")

    /**
     * Snapshots get a version name that does not change with new commits, so that users
     * with projects that are dependent on snapshots don't have to change the build.gradle.kts
     * file all the time.
     * Non-snapshots have a non-changing name, so that production or qa releases have a
     * fixed dependency on a particular version of the library
     *
     */
    @JvmStatic
    fun versionName(): String {

        val cmdOutput = if (isSnapshot)
            "$shortVersionName-SNAPSHOT"
        else
            longVersionName

        check(cmdOutput.matches(
            versionFormat) || cmdOutput == "") { "Wrong git version: \"$cmdOutput\" " }

        return if (cmdOutput.isEmpty()) "v0.0-nogit" else cmdOutput
    }

    /**
     * The version code is an integer that is based on the version name.  The version
     * name must have the form `v1.0-n-m` with n being an integer and m a hex string.
     * Only the majr and minor version numbers are used for the version code. The minor version
     * must not exceed 99.
     * v1.0 becomes 100
     * v1.1 becomes 101
     * v1.2 becomes 102
     * v99.99 becomes 9999
     * etc
     */
    @JvmStatic
    fun versionCode(): Int {

        val matchResult = versionFormat.find(versionName())
                ?: throw IllegalStateException("Wrong git version: \"${versionName()}\" ")

        val (major, minor) = matchResult.destructured

        return major.toInt() * 100 + minor.toInt()
    }

    @JvmStatic
    private fun runSystemCmd(cmd: String) =
        try {
            val process = Runtime.getRuntime().exec(cmd)

            process.waitFor(2L, TimeUnit.SECONDS)
            process
                    .inputStream
                    .reader(Charset.defaultCharset())
                    .readText()
                    .dropLastWhile { it == '\n' }
        } catch (e: Exception) {
            ""
        }

    @JvmStatic
    fun describe(): String {
        return """
            |Build Configuration
            |===============================================
            |Debug:                 $isDebug
            |Snapshot:              $isSnapshot
            |Long version:          $longVersionName
            |Short version:         $shortVersionName
            |-----
            |Group:                 $group
            |Version:               ${versionName()}
            |Version Code:          ${versionCode()}
            |Artifactory Repo key   ${artifactoryRepoKey}
            |================================================
        """.trimMargin()
    }
}
