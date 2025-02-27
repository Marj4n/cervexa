plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.gizthon.camera"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.gizthon.camera"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.media)
    implementation(libs.commons.net)
    implementation(libs.library)
    implementation(libs.logging.interceptor)
    implementation(libs.preference)
    implementation(libs.util)
    implementation(libs.swiperefreshlayout)
    implementation(libs.core)
    implementation(libs.glide)
    implementation(libs.mpandroidchart)
    implementation(libs.constraintlayout)
    implementation(libs.constraintlayout.solver)
}