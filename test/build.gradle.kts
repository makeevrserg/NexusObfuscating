import ru.astrainteractive.gradleplugin.util.ProjectProperties.projectInfo

plugins {
    kotlin("jvm")
    application
}

dependencies {
    implementation("com.makeevrserg.sample:api:${projectInfo.versionString}")
}

application {
    mainClass.set("com.makeevrserg.sample.Main")
}
