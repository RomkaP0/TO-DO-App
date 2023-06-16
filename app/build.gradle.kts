@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.com.google.dagger.hilt.android)
    alias(libs.plugins.parcelize)
    id ("androidx.navigation.safeargs.kotlin")

}

android {
    namespace = "com.romka_po.to_doapp"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.romka_po.to_doapp"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        compileSdkPreview = "UpsideDownCake"

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
    sourceSets {
        getByName("main").java.srcDirs("build/generated/source/navigation-args")
    }
}

dependencies {

    kapt(libs.hilt.compiler)
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.hilt)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
kapt{
    correctErrorTypes = true
}