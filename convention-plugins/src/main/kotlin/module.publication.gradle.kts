import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.`maven-publish`

plugins {
    `maven-publish`
    signing
}

publishing {
    // Configure all publications
    publications.withType<MavenPublication> {
        // Stub javadoc.jar artifact
        artifact(tasks.register("${name}JavadocJar", Jar::class) {
            archiveClassifier.set("javadoc")
            archiveAppendix.set(this@withType.name)
        })

        // Provide artifacts information required by Maven Central
        pom {
            name.set("hexutils library")
            description.set("Functions to convert hex character string to byte or int lists and vice versa.")
            url.set("https://github.com/ingonoka/hexutils-mp")

            licenses {
                license {
                    name.set("Attribution-NonCommercial-NoDerivatives 4.0 International (CC BY-NC-ND 4.0)")
                    url.set("https://creativecommons.org/licenses/by-nc-nd/4.0/")
                }
            }
            developers {
                developer {
                    id.set("Ingo Noka")
                    name.set("Ingo Noka")
                    organization.set("Ingo Noka")
                    organizationUrl.set("https://com.github/ingonoka")
                }
            }
            scm {
                url.set("https://github.com/ingonoka/hexutils-mp")
            }
        }
    }
}

signing {
    if (project.hasProperty("signing.gnupg.keyName")) {
        useGpgCmd()
        sign(publishing.publications)
    }
}
