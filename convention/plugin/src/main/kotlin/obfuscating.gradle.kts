import com.android.build.gradle.internal.tasks.factory.dependsOn
import com.android.build.gradle.tasks.BundleAar
import com.makeevrserg.sample.Constants.OBFUSCATING_TASK_NAME
import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import proguard.gradle.ProGuardTask

plugins {
    kotlin("multiplatform")
    id("com.android.library")
}
val rules = file("proguard-rules.pro")
fun log(message: String) {
    logger.warn("MY_TAG-OBF: $message")
}

fun ProGuardTask.obfuscateOutFile(task: Task) {
    val jars = task.outputs.files
    log("Got files: ${jars.map { it.name }}")
    injars(jars)
    val outJars = jars.map {
        File(it.parentFile, "obf-${it.name}")
    }
    log("Got outJars files: ${outJars.map { it.absolutePath }}")
    outjars(outJars)
}

val obfuscateTask = tasks.register(OBFUSCATING_TASK_NAME, ProGuardTask::class) {
    log("Registered OBFUSCATING_TASK_NAME")
    val targets = kotlin.targets
    log("Got OBF targets: ${targets.size}")
    targets.forEach {
        when (it) {
            is KotlinJvmTarget -> {
                log("KotlinJvmTarget ${it.artifactsTaskName}")
                val task = tasks.getByName(it.artifactsTaskName)
                obfuscateOutFile(task)
            }

            is KotlinAndroidTarget -> {
                val aarTasks = tasks.withType(BundleAar::class)
                aarTasks.forEach { aarTask -> obfuscateOutFile(aarTask) }
            }
        }
    }


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
