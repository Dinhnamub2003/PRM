plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.project_prm"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.project_prm"
        minSdk = 27
        targetSdk = 34
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(platform(libs.firebase.bom))

    // Room Database
    implementation(libs.room.runtime)
    implementation(libs.firebase.auth)
    annotationProcessor(libs.room.compiler)
    implementation(libs.firebase.auth.v2231)
    implementation(libs.play.services.auth)

    // DataStore
    implementation(libs.datastore.core.android)

    // Lombok
    implementation(libs.lombok)
    annotationProcessor(libs.lombok)

    // Unit Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
