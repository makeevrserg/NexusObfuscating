
import ru.astrainteractive.gradleplugin.util.ProjectProperties.projectInfo
import ru.astrainteractive.gradleplugin.util.ProjectProperties.publishInfo
plugins {
    id("org.gradle.maven-publish")
    id("signing")
}
publishing {
    repositories {
        maven {
            name = "maven-releases"
            isAllowInsecureProtocol = true
            setUrl("http://178.32.231.56:7777/repository/maven-releases/")
            credentials {
                username = publishInfo.ossrhUsername
                password = publishInfo.ossrhPassword
            }
        }
    }

    // Default configuration for every maven publication
    publications.withType<MavenPublication>().all {

        pom {
            this.name.set(publishInfo.libraryName)
            this.description.set(projectInfo.description)
            this.url.set(publishInfo.gitHubUrl)
            groupId = publishInfo.publishGroupId
            licenses {
                license {
                    this.name.set(publishInfo.license)
                    this.distribution.set("repo")
                    this.url.set("${publishInfo.gitHubUrl}/blob/master/LICENSE.md")
                }
            }

            developers {
                projectInfo.developersList.forEach { dev ->
                    developer {
                        id.set(dev.id)
                        name.set(dev.name)
                        email.set(dev.email)
                    }
                }
            }

            scm {
                this.connection.set(publishInfo.sshUrl)
                this.developerConnection.set(publishInfo.sshUrl)
                this.url.set(publishInfo.gitHubUrl)
            }
        }
    }
}
// Default configuration for maven publish
configure<SigningExtension> {
    useInMemoryPgpKeys(
        publishInfo.signingKeyId,
        publishInfo.signingKey,
        publishInfo.signingPassword
    )
    sign(extensions.getByType<PublishingExtension>().publications)
}
