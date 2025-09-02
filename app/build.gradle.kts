plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.mine.expensetracker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.mine.expensetracker"
        minSdk = 28
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }

    viewBinding {
        enable = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation (libs.androidx.constraintlayout.compose)

    // Lifecycle ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // LiveData
    implementation(libs.androidx.lifecycle.livedata.ktx)

    //Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation (libs.coil.compose)

    //Layout
    implementation (libs.androidx.constraintlayout)

    //Lottie
    implementation(libs.lottie)
    implementation(libs.lottie.compose)

    //Material 3
    implementation(libs.material3)
    implementation(libs.material)
    implementation (libs.androidx.material3.v121)

    //Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)
    implementation(libs.play.services.auth)
    implementation(libs.firebase.bom.v3320)
//    implementation("com.google.firebase:firebase-auth")
    implementation(libs.firebase.database)

    //Hilt
    implementation (libs.androidx.hilt.navigation.compose)
    implementation (libs.hilt.android)

    //Gson
    implementation (libs.gson)

    //PieChart
    implementation (libs.mpandroidchart)

    // Room components
    implementation (libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation (libs.androidx.room.ktx)
}