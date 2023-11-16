
import ru.astrainteractive.gradleplugin.util.ProjectProperties.projectInfo

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("publish")
    id("obfuscating")
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
