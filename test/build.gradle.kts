import ru.astrainteractive.gradleplugin.util.ProjectProperties.projectInfo

plugins {
    kotlin("jvm")
    application
}

dependencies {
    // Coroutines
    implementation(libs.kotlin.coroutines.coreJvm)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.okio)
    implementation("com.makeevrserg.sample:api:${projectInfo.versionString}")
}

application {
    mainClassName = "com.makeevrserg.sample.Main"
}
