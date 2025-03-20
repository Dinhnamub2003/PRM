plugins {
    alias(libs.plugins.android.application)
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
    implementation ("androidx.paging:paging-runtime:3.1.1")
    implementation(platform(libs.firebase.bom))
    // Room Database
    implementation(libs.room.runtime)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.firebase.auth)
    annotationProcessor(libs.room.compiler)
    implementation(libs.firebase.auth.v2231)
    implementation(libs.play.services.auth)

    // DataStore
    implementation(libs.datastore.core.android)

    // Chart
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // Lombok
    implementation("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    // Unit Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Live
    implementation("com.github.ZEGOCLOUD:zego_uikit_prebuilt_live_streaming_android:+")
}
