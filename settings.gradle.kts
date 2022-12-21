rootProject.name = "Flex Tracker"
rootProject.buildFileName = "build.gradle.kts"

include(":app")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
