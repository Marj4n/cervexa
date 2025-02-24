plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.marjan.cervexa"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.marjan.cervexa"
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
    aaptOptions {
        noCompress(".*")
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.media)
    implementation(libs.volley)
    //noinspection DuplicatePlatformClasses
    implementation(libs.httpclient)
    implementation(libs.constraintlayout.solver)
    implementation(libs.util)
    implementation(libs.api.explorer.sdk)
    implementation(libs.databinding.runtime)
    implementation(libs.swiperefreshlayout)
    implementation(libs.preference)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    implementation(libs.material)
    androidTestImplementation(libs.espresso.core)
}