plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "se.yverling.flextracker"

    compileSdk = rootProject.extra.get("compileSdkVersion") as Int

    defaultConfig {
        applicationId = "se.yverling.flextracker"
        minSdk = rootProject.extra.get("minSdkVersion") as Int
        targetSdk = rootProject.extra.get("targetSdkVersion") as Int
        versionCode = 20000 // Version & release number
        versionName = "2.0.0"
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = libs.versions.kotlinOptionsJvmTarget.get()
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.kotlinCompiler.get()
    }
}

dependencies {
    implementation(libs.appCompat)

    kapt(libs.hilt.android.compiler)
    implementation(libs.bundles.hilt)

    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)

    implementation(libs.dataStore.preferences)
}