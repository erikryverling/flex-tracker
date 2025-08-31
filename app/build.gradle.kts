import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.android.kotlin)
    alias(libs.plugins.hilt)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
}

android {
    namespace = "se.yverling.flextracker"

    compileSdk = Versions.compileSdk

    defaultConfig {
        minSdk = Versions.minSdk

        applicationId = "se.yverling.flextracker"
        versionCode = 20000 // Version & release number
        versionName = "2.0.0"
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
    }
}

dependencies {
    implementation(libs.appCompat)

    ksp(libs.hilt.android.compiler)
    implementation(libs.bundles.hilt)

    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)

    implementation(libs.dataStore.preferences)
}
