import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION")
buildscript {
    dependencies {

        classpath(libs.navigation.safe.args.gradle.plugin)
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.navigation.safe.args) apply false
    alias(libs.plugins.kotlin.serialize) apply false
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        version.set("0.49.1")
        android.set(true)
        verbose.set(true)
        reporters{
            reporter(ReporterType.HTML)
        }
        filter{
            include("**/*.kt")
            exclude("**/build/**")
        }
    }
}
true