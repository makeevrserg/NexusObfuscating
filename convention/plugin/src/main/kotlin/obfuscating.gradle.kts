import com.android.build.gradle.internal.tasks.factory.dependsOn
import com.android.build.gradle.tasks.BundleAar
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
    kotlin.targets.forEach {
        fun obfuscateOutFile(task: Task) {
            val jars = task.outputs.files
            injars(jars)
            val outJars = jars.map {
                File(it.parentFile, "obf-${it.name}")
            }
            outjars(outJars)

        }
        when (it) {
            is KotlinJvmTarget -> {
                val task = tasks.getByName(it.artifactsTaskName)
                obfuscateOutFile(task)
            }

            is KotlinAndroidTarget -> {
                val aarTasks = tasks.withType(BundleAar::class)
                aarTasks.forEach(::obfuscateOutFile)
            }
        }
    }
}

afterEvaluate {
    tasks.withType(proguard.gradle.ProGuardTask::class) {
        verbose()

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
