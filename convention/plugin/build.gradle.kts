plugins {
    `kotlin-dsl`
}
dependencies {
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.android.toolsBuild)
    compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation("com.guardsquare:proguard-gradle:7.4.0")
    implementation("ru.astrainteractive.gradleplugin:convention:0.4.0")
    implementation("ru.astrainteractive.gradleplugin:android:0.4.0")
}
