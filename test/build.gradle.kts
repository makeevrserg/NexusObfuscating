import ru.astrainteractive.gradleplugin.util.ProjectProperties.projectInfo

plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

kotlin {
    jvm()
    android()
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("com.makeevrserg.sample:api:${projectInfo.versionString}")
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("com.makeevrserg.sample:api-android:${projectInfo.versionString}")
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("com.makeevrserg.sample:api-jvm:${projectInfo.versionString}")
            }
        }
    }
}
