buildscript {
    dependencies {
        classpath("ru.astrainteractive.gradleplugin:convention:0.4.0")
        classpath("ru.astrainteractive.gradleplugin:minecraft:0.4.0")
    }
}

plugins {
    java
    `java-library`
    alias(libs.plugins.kotlin.multiplatform) apply false
}

apply(plugin = "ru.astrainteractive.gradleplugin.detekt")
apply(plugin = "ru.astrainteractive.gradleplugin.root.info")

subprojects.forEach {
    it.plugins.withId("org.jetbrains.kotlin.jvm") {
        it.apply(plugin = "ru.astrainteractive.gradleplugin.java.core")
    }
}
