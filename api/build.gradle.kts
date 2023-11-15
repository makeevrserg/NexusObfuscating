import com.android.build.gradle.internal.tasks.factory.dependsOn
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import ru.astrainteractive.gradleplugin.util.ProjectProperties.projectInfo
import ru.astrainteractive.gradleplugin.util.ProjectProperties.publishInfo

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.gradle.maven-publish")
    id("signing")
}

kotlin {
    withSourcesJar(false)
    jvm()
    androidTarget {
        publishLibraryVariants("release", "debug")
        publishLibraryVariantsGroupedByFlavor = true
    }
}

android {
    namespace = "${projectInfo.group}.api"
}

val OBFUSCATING_TASK_NAME = "OBFUSCATING_TASK_NAME"

// Obfuscating ----------------------------------------------------------------------

val rules = file("proguard-rules.pro")

val obfuscateTask = tasks.register(OBFUSCATING_TASK_NAME, proguard.gradle.ProGuardTask::class) {
    verbose()
    kotlin.targets.map(KotlinTarget::artifactsTaskName)
        .mapNotNull { tasks.findByName(it) }
        .takeIf(Collection<*>::isNotEmpty)
        ?.let { dependsOn(it) }

    kotlin
        .targets
        .forEach {
            val targetName = it.targetName.toLowerCase()
            when (it) {
                is KotlinJvmTarget -> {
                    injars("$buildDir/libs/${project.name}-$targetName-$version.jar")
                    outjars("$buildDir/libs/obfuscated/${project.name}-$targetName-$version.jar")
                }

                is KotlinAndroidTarget -> {
                    injars("$buildDir/outputs/aar/${project.name}-debug.aar")
                    outjars("$buildDir/outputs/aar/obfuscated/${project.name}-debug.aar")

                    injars("$buildDir/outputs/aar/${project.name}-release.aar")
                    outjars("$buildDir/outputs/aar/obfuscated/${project.name}-release.aar")
                }
            }
        }

    adaptresourcefilenames()
    adaptresourcefilecontents()
    optimizationpasses(9)
    allowaccessmodification()
    mergeinterfacesaggressively()

    printseeds("$buildDir/obfuscated/seeds.txt")
    printmapping("$buildDir/obfuscated/mapping.txt")
    if (rules.exists()) {
        configuration(rules)
    }
}

// Setup task dependencies
afterEvaluate {
    obfuscateTask.dependsOn(tasks.getByName("bundleDebugAar"))
    obfuscateTask.dependsOn(tasks.getByName("bundleReleaseAar"))
    obfuscateTask.dependsOn(tasks.getByName("jvmJar"))
}

// Publishing ------------------------------------------------------------

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

    publications.register<MavenPublication>("jvm-obfuscated") {
        val artifactName = "${project.name}-jvm-${project.version}.jar"
        artifact("${project.buildDir}/libs/obfuscated/$artifactName") {
            artifactId = "api-jvm"
            builtBy(obfuscateTask)
            extension = "jar"
            classifier = "jar"
        }
    }

    publications.register<MavenPublication>("android-obfuscated") {
        val debugArtifactName = "${project.name}-debug.aar"
        val releaseArtifactName = "${project.name}-release.aar"
        artifact("$buildDir/outputs/aar/obfuscated/$debugArtifactName") {
            artifactId = "api-android"
            classifier = "debug"
            builtBy(obfuscateTask)
        }
        artifact("$buildDir/outputs/aar/obfuscated/$releaseArtifactName") {
            artifactId = "api-android"
            classifier = "release"
            builtBy(obfuscateTask)
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
