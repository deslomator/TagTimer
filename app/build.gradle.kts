import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("android")
    id("com.android.application")
    kotlin("plugin.serialization")
    kotlin("plugin.compose")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.deslomator.tagtimer"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.deslomator.tagtimer"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
//            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
//        isCoreLibraryDesugaringEnabled = true

        buildFeatures {
            compose = true
        }
        composeOptions {
            kotlinCompilerExtensionVersion = "1.5.0"
        }
        packaging {
            resources {
                excludes.add("/META-INF/{AL2.0,LGPL2.1}")
            }
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
        javaParameters.set(true)
    }
}

dependencies {

    implementation(platform("org.jetbrains.kotlin:kotlin-bom:2.1.10"))
    implementation(platform("androidx.compose:compose-bom:2025.02.00"))
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.activity:activity-compose:1.10.1")

    implementation("androidx.compose.foundation:foundation:1.7.8")
    implementation("androidx.compose.ui:ui-android:1.7.8")
    implementation("androidx.compose.ui:ui-graphics-android:1.7.8")
    implementation("androidx.compose.ui:ui-tooling-preview-android:1.7.8")
    implementation("androidx.compose.material3:material3:1.3.1")

    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

    implementation("androidx.navigation:navigation-compose:2.8.8")
    implementation("androidx.navigation:navigation-runtime-ktx:2.8.8")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    // room
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    // DocumentFile
    implementation("androidx.documentfile:documentfile:1.0.1")


    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2025.02.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4-android:1.7.8")

    debugImplementation("androidx.compose.ui:ui-tooling-android:1.7.8")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.7.8")
}