import ru.astrainteractive.gradleplugin.util.ProjectProperties.projectInfo
import ru.astrainteractive.gradleplugin.util.ProjectProperties.publishInfo

plugins {
    kotlin("jvm")
    id("org.gradle.maven-publish")
    id("signing")
    application
}

dependencies {
    // Coroutines
    implementation(libs.kotlin.coroutines.coreJvm)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.okio)
}

application {
    mainClassName = "com.makeevrserg.sample.Main"
}

// val javadocJar = tasks.register("javadocJar", Jar::class) {
//    archiveClassifier.set("javadoc")
// }

// configure<PublishingExtension> {
//    publications.withType<MavenPublication> {
//        artifact(javadocJar.get())
//    }
// }
// configure<JavaPluginExtension> {
//    kotlin.runCatching {
//        withSourcesJar()
//        withJavadocJar()
//    }
// }
// tasks.withType<JavaCompile> {
//    options.encoding = "UTF-8"
// }
// plugins.withId("org.gradle.maven-publish") {
//    configure<PublishingExtension> {
//        publications.register("mavenJava", MavenPublication::class) {
//            from(components["java"])
//        }
//    }
// }

val projectName = name
configure<PublishingExtension> {
    repositories {

        maven {
            name = "maven-releases"
            isAllowInsecureProtocol = true
            setUrl("http://178.32.231.56:7777/repository/maven-snapshots/")
            credentials {
                username = publishInfo.ossrhUsername
                password = publishInfo.ossrhPassword
            }
        }
    }
    publications.create<MavenPublication>("default")
    publications.withType<MavenPublication> {
        pom {
            this.name.set(publishInfo.libraryName)
            this.description.set(projectInfo.description)
            this.url.set(publishInfo.gitHubUrl)
            groupId = publishInfo.publishGroupId
            artifactId = projectName
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

// configure<SigningExtension> {
//    useInMemoryPgpKeys(publishInfo.signingKeyId, publishInfo.signingKey, publishInfo.signingPassword)
//    sign(extensions.getByType<PublishingExtension>().publications)
// }
