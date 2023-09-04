@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlin.serialize)
    alias(libs.plugins.ktlint)
}

android {
    namespace = "com.romkapo.todoapp"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.romkapo.todoapp"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        compileSdkPreview = "UpsideDownCake"

        manifestPlaceholders["YANDEX_CLIENT_ID"] = "3a2ee2f9992946e193f7160fd9870bc5"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
    sourceSets {
        getByName("main").java.srcDirs("build/generated/source/navigation-args")
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
}

dependencies {
    val composeBom = "androidx.compose:compose-bom:2023.05.01"

    ksp(libs.room.compiler)
    kapt(libs.bundles.dagger.compiler)

    implementation(platform(composeBom))
    androidTestImplementation(platform(composeBom))

    // material
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material)
    implementation(libs.androidx.material.icons.core)
    implementation(libs.androidx.material.icons.extended)

    // compose_integration
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Android Studio Preview support
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)

    // UI Tests
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.bundles.dagger)
    implementation(libs.bundles.retrofit)
    implementation(libs.bundles.okhttp)
    implementation(libs.info.bar.compose)

    implementation(libs.accompanist.permissions)

    implementation(libs.androidx.activity.ktx)

    implementation(libs.androidx.swiperefreshlayout)

    implementation(libs.androidx.work.runtime.ktx)

    implementation(libs.core.ktx)

    implementation(libs.appcompat)
    implementation(libs.material)

    implementation(libs.room.runtime)
    implementation(libs.room)

    implementation(libs.kotlin.serialaize.json)

    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.authsdk)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
kapt {
    correctErrorTypes = true
}
