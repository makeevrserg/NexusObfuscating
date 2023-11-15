import ru.astrainteractive.gradleplugin.util.ProjectProperties.projectInfo

plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

kotlin {
    jvm()
    android()
    ios()
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("com.makeevrserg.sample:api:${projectInfo.versionString}")
            }
        }
    }
}
