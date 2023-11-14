plugins {
    kotlin("jvm")
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
