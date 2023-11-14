pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        maven("http://178.32.231.56:7777/repository/maven-snapshots/") {
            isAllowInsecureProtocol = true
        }
        maven("http://178.32.231.56:7777/repository/maven-releases/") {
            isAllowInsecureProtocol = true
        }
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "NexusObfuscating"

include("api")
include("test")
