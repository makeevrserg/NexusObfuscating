pluginManagement {
    repositories {
        google()
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositories {
        google()
        mavenLocal()
        mavenCentral()
        maven("http://178.32.231.56:7777/repository/maven-snapshots/") {
            isAllowInsecureProtocol = true
        }
        maven("http://178.32.231.56:7777/repository/maven-releases/") {
            isAllowInsecureProtocol = true
        }
    }
    versionCatalogs { create("libs") { from(files("../gradle/libs.versions.toml")) } }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "convention"

include("plugin")
