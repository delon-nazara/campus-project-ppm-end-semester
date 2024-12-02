plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    // Google services for firebase
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.proyekakhirlabpemrogramanmobile"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.proyekakhirlabpemrogramanmobile"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Default library
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Firebase library
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth.ktx)

    // CameraX library
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.extensions)
    implementation(libs.guava)

    // Cloudinary library
    implementation(libs.cloudinary.android)

    // Dotenv kotlin library
    implementation(libs.dotenv.kotlin)

    // Image cropper library
    implementation(libs.android.image.cropper)

    // Glide library
    implementation(libs.glide)
    annotationProcessor(libs.compiler)
}