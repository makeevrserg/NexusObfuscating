buildscript {
    dependencies {
        classpath("ru.astrainteractive.gradleplugin:convention:0.4.0")
        classpath("ru.astrainteractive.gradleplugin:android:0.4.0")
        classpath("com.guardsquare:proguard-gradle:7.4.0")
    }
}

plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.gradle.android) apply false
}

apply(plugin = "ru.astrainteractive.gradleplugin.dokka.root")
apply(plugin = "ru.astrainteractive.gradleplugin.detekt")
apply(plugin = "ru.astrainteractive.gradleplugin.root.info")

subprojects.forEach {
    it.apply(plugin = "ru.astrainteractive.gradleplugin.dokka.module")
    it.plugins.withId("org.jetbrains.kotlin.jvm") {
        it.apply(plugin = "ru.astrainteractive.gradleplugin.java.core")
    }
    it.apply(plugin = "ru.astrainteractive.gradleplugin.stub.javadoc")
    it.plugins.withId("com.android.library") {
        it.apply(plugin = "ru.astrainteractive.gradleplugin.android.core")
    }
}
