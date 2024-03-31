
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

//    For Hilt Plugin
    id("dagger.hilt.android.plugin")
//    For Kotlin Plugin
    id("kotlin-kapt")
}

android {
    namespace = "com.jeyendroid.sciflare_assessment_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.jeyendroid.sciflare_assessment_app"
        minSdk = 24
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
//For DataBinding
    buildFeatures {
        dataBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.5"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Room Database
    implementation("androidx.room:room-runtime:2.6.1")
    kapt ("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")


    // to create singleton instance in activity and fragment
    implementation ("androidx.activity:activity-ktx:1.7.2")
    implementation ("androidx.fragment:fragment-ktx:1.6.1")

    // HILT for dependency injection
    implementation ("com.google.dagger:hilt-android:2.47")
    kapt ("com.google.dagger:hilt-android-compiler:2.47")

//    For retrofit api background call task
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:4.9.0")

//    MapView
    implementation ("com.google.android.gms:play-services-maps:17.0.0")
    implementation ("com.google.android.gms:play-services-location:17.0.0")
    implementation ("com.google.android.gms:play-services-maps:17.0.0")
}