import com.android.build.gradle.tasks.BundleAar
import com.makeevrserg.sample.Constants
import gradle.kotlin.dsl.accessors._6c349c166eb4771ad5f15cb2a7e2eca9.kotlin
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import ru.astrainteractive.gradleplugin.util.ProjectProperties.projectInfo
import ru.astrainteractive.gradleplugin.util.ProjectProperties.publishInfo

plugins {
    id("org.gradle.maven-publish")
    id("signing")
}
afterEvaluate {

    configure<PublishingExtension> {
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
        val obfuscateTask = tasks.getByName(Constants.OBFUSCATING_TASK_NAME)

        publications.register<MavenPublication>("jvm-obfuscated") {
            println("MY_TAG: jvm-obfuscated")
            kotlin.targets.filterIsInstance<KotlinJvmTarget>().forEach { target ->
                println("MY_TAG: ${target.targetName}")
                tasks.getByName(target.artifactsTaskName).outputs.files.forEach { file ->
                    val artifact = File(file.parentFile, "obf-${file.name}")
                    artifact(artifact) {
                        builtBy(obfuscateTask)
                        artifactId = "${project.name}-${target.targetName.toLowerCase()}"
                        extension = "jar"
                        classifier = "jar"
                    }
                }
            }
        }

        publications.register<MavenPublication>("android-obfuscated") {
            kotlin.targets.filterIsInstance<KotlinAndroidTarget>().forEach { target ->
                val tasks = tasks.withType(BundleAar::class)

                tasks.onEach {
                    println("MY_TAG: ${it.name} -> ${it.variantName} -> ${it::class}")
                }
                tasks.map { it.outputs.files to it.variantName }.forEach { (files, variant) ->
                    files.filter { it.parentFile.name == "aar" }.forEach { file ->
                        println("MY_TAG: file: ${file.absolutePath}")

                        val artifact = File(file.parentFile, "obf-${file.name}")
                        artifact(artifact) {
                            builtBy(obfuscateTask)
                            artifactId = "${project.name}-${target.targetName.toLowerCase()}"
                            extension = "${file.extension}"
                            classifier = variant
                        }
                    }
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
//    tasks.withType<GenerateModuleMetadata> {
//        enabled = false
//    }
}