plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.20"
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21"
    id("com.google.gms.google-services")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.com"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.top.example"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            val prodUrl = (project.findProperty("PROD_BASE_URL") as String?)
                ?: "https://example.com/"
            val prodNorm = if (prodUrl.endsWith("/")) prodUrl else "$prodUrl/"

            resValue("string", "base_url", prodNorm)
            buildConfigField("String", "BASE_URL", "\"$prodNorm\"")
            resValue("string", "app_name", "Top Serviços")
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            val prodUrl = (project.findProperty("PROD_BASE_URL") as String?)
                ?: "https://example.com/"
            val prodNorm = if (prodUrl.endsWith("/")) prodUrl else "$prodUrl/"

            resValue("string", "base_url", prodNorm)
            buildConfigField("String", "BASE_URL", "\"$prodNorm\"")
            resValue("string", "app_name", "Top Serviços")

            ndk { debugSymbolLevel = "SYMBOL_TABLE" }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions { jvmTarget = "17" }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.2")

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.activity:activity-compose:1.9.2")

    // Compose BOM
    implementation(platform("androidx.compose:compose-bom:2024.09.03"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.foundation:foundation")

    // Navigation + outros
    implementation("androidx.navigation:navigation-compose:2.8.0")
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4")
    implementation("androidx.lifecycle:lifecycle-process:2.8.4")
    implementation("androidx.exifinterface:exifinterface:1.3.7")

    // Retrofit/OkHttp
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")

    // Coil
    implementation("io.coil-kt:coil-compose:2.6.0")


    // Billing

    implementation("com.android.billingclient:billing-ktx:7.0.0")

    // 🔥 Firebase (use BOM para manter versões compatíveis)
    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))
    implementation("com.google.firebase:firebase-messaging-ktx")



    // GARANTE fragment >= 1.3 (use a última estável)
    implementation("androidx.fragment:fragment-ktx:1.8.3")

    implementation ("com.github.yalantis:ucrop:2.2.8")



    // Play Services Location se precisar
    implementation("com.google.android.gms:play-services-location:21.3.0")

    implementation("com.google.android.play:review:2.0.1")
    implementation("com.google.android.play:review-ktx:2.0.1")

    implementation("com.google.android.gms:play-services-ads:23.1.0")


    implementation("androidx.media3:media3-exoplayer:1.2.1")
    implementation("androidx.media3:media3-ui:1.2.1")

    // Coroutines + Play Services (para await())
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")

    implementation("com.google.android.material:material:1.12.0")

    implementation("com.google.accompanist:accompanist-permissions:0.30.1")



    implementation("androidx.compose.animation:animation")

    // Testes / debug
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.09.03"))
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation("com.facebook.android:facebook-android-sdk:latest.release")
    implementation("com.facebook.android:facebook-core:latest.release")
    implementation("com.facebook.android:facebook-applinks:latest.release")

}
