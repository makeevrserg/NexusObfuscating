import com.android.build.gradle.internal.tasks.factory.dependsOn
import com.makeevrserg.sample.Constants.OBFUSCATING_TASK_NAME
import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

plugins {
    kotlin("multiplatform")
    id("com.android.library")
}
val rules = file("proguard-rules.pro")

val obfuscateTask = tasks.register(OBFUSCATING_TASK_NAME, proguard.gradle.ProGuardTask::class) {
    verbose()
    kotlin
        .targets
        .forEach {
            val targetName = it.targetName.toLowerCase()
            when (it) {
                is KotlinJvmTarget -> {
                    val original = "$buildDir/libs/${project.name}-$targetName-$version.jar"
                    val originalRenamed =
                        file("$buildDir/libs/orig-${project.name}-$targetName-$version.jar")
                    originalRenamed.parentFile.mkdirs()
                    file(original).renameTo(originalRenamed)
                    injars(originalRenamed)
                    outjars(file(original))
                }

                is KotlinAndroidTarget -> {

                    run {
                        val original = "$buildDir/outputs/aar/${project.name}-debug.aar"
                        val originalRenamed =
                            file("$buildDir/outputs/aar/orig-${project.name}-debug.aar")
                        originalRenamed.parentFile.mkdirs()
                        file(original).renameTo(originalRenamed)
                        injars(originalRenamed)
                        outjars(file(original))
                    }

                    run {
                        val original = "$buildDir/outputs/aar/${project.name}-release.aar"
                        val originalRenamed =
                            file("$buildDir/outputs/aar/orig-${project.name}-release.aar")
                        originalRenamed.parentFile.mkdirs()
                        file(original).renameTo(originalRenamed)
                        injars(originalRenamed)
                        outjars(file(original))
                    }
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
    kotlin.targets.map(KotlinTarget::artifactsTaskName)
        .mapNotNull { tasks.findByName(it) }
        .map { tasks.named(it.name) }
        .takeIf(Collection<*>::isNotEmpty)
        ?.let { obfuscateTask.dependsOn(it) }

    obfuscateTask.dependsOn(
        tasks.named("bundleDebugAar"),
        tasks.named("bundleReleaseAar"),
        tasks.named("jvmJar")
    )
    tasks.findByName("publish")?.dependsOn(obfuscateTask)
}
