import com.android.build.gradle.tasks.BundleAar
import gradle.kotlin.dsl.accessors._6c349c166eb4771ad5f15cb2a7e2eca9.kotlin
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

plugins {
    id("org.gradle.maven-publish")
    id("signing")
}


configure<PublishingExtension> {

    publications.register<MavenPublication>("jvm-obfuscated") {
        artifactId = "${project.name}-jvm"
        println("MY_TAG: jvm-obfuscated")
        afterEvaluate {
            val obfuscateTask =
                tasks.getByName(com.makeevrserg.sample.Constants.OBFUSCATING_TASK_NAME)
            val kjvm = kotlin.targets.filterIsInstance<KotlinJvmTarget>()
            println("MY_TAG: KJVM ${kjvm.size}")
            kjvm.forEach { target ->
                println("MY_TAG: Processing ${target.name}")
                tasks.getByName(target.artifactsTaskName).outputs.files.forEach { file ->
                    val artifact = File(file.parentFile, "obf-${file.name}")
                    println("MY_TAG: Processing artifact ${artifact.absolutePath}")
                    artifact(artifact) {
                        builtBy(obfuscateTask)
                        extension = artifact.extension
                        classifier = artifact.extension
                    }
                }
            }
        }
    }

    publications.register<MavenPublication>("android-obfuscated") {
        artifactId = "${project.name}-android"

        afterEvaluate {
            val obfuscateTask =
                tasks.getByName(com.makeevrserg.sample.Constants.OBFUSCATING_TASK_NAME)
            kotlin.targets.filterIsInstance<KotlinAndroidTarget>().forEach { target ->
                val tasks = tasks.withType(BundleAar::class)

                tasks.map { it.outputs.files to it.variantName }.forEach { (files, variant) ->
                    files.filter { it.parentFile.name == "aar" }.forEach { file ->
                        val artifact = File(file.parentFile, "obf-${file.name}")
                        artifact(artifact) {
                            builtBy(obfuscateTask)
                            extension = file.extension
                            classifier = variant
                        }
                    }
                }
            }
        }

    }
}
