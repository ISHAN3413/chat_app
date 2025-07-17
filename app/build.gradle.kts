import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.android.manifmerger.Actions.load
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.plugin.serialization")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.globalchat"
    compileSdk = 35

    buildFeatures {
        buildConfig = true
    }

    val localProperties = Properties().apply {
        load(rootProject.file("local.properties").inputStream())
    }

    val key: String = localProperties.getProperty("supabaseKey") ?: ""
    val url: String = localProperties.getProperty("supabaseUrl") ?: ""
    val gkey: String = localProperties.getProperty("geminikey") ?: ""

    defaultConfig {
        applicationId = "com.example.globalchat"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "SUPABASE_KEY", "\"$key\"")
        buildConfigField("String", "SUPABASE_URL", "\"$url\"")
        buildConfigField("String", "GEMINI_KEY", "\"$gkey\"")
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
}

dependencies {
    val room = "2.6.0"
    //ROOM
    implementation("androidx.room:room-runtime:$room")
    implementation("androidx.room:room-ktx:$room")
    kapt("androidx.room:room-compiler:$room")

    //supabase dependencies
    val compose_version = "1.6.0-alpha08"
    implementation("io.github.jan-tennert.supabase:postgrest-kt:1.3.2") // Database
    implementation ("io.github.jan-tennert.supabase:gotrue-kt:1.4.0") // Authentication
    implementation  ("io.github.jan-tennert.supabase:storage-kt:1.4.0")
    implementation  ("io.github.jan-tennert.supabase:realtime-kt:1.4.0")
    implementation ("io.ktor:ktor-client-cio:2.3.4") // HTTP client
    implementation("io.coil-kt:coil-compose:2.2.0")
    implementation("androidx.compose.ui:ui:$compose_version")
    implementation("androidx.compose.material:material:$compose_version")
    implementation("androidx.compose.ui:ui-tooling-preview:$compose_version")
    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.30.1")
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.runtime.android)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.play.services.auth)
    implementation(libs.androidx.runtime.livedata)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    //firebase
}