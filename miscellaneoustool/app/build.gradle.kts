plugins {
    id("jinProject.android.application")
    id("jinProject.android.compose")
    id("jinProject.android.parcelize")
    id("jinProject.android.gms-services")
    alias(libs.plugins.android.navigation.safeargs.kotlin)
}

android {
    namespace = "com.jinproject.twomillustratedbook"

    defaultConfig {
        applicationId = "com.jinproject.twomillustratedbook"
        targetSdk = 34
        versionCode = 35
        versionName = "2.2.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":core"))
    implementation(project(":features:collection"))
    implementation(project(":features:droplist"))
    implementation(project(":features:alarm"))
    implementation(project(":features:watch"))
    implementation(project(":features:gear"))
    implementation(project(":features:core"))
    implementation(project(":features:home"))
    implementation(project(":features:symbol"))
    implementation(project(":design-compose"))
    implementation(project(":design-ui"))

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity.compose)

    implementation(libs.bundles.navigation)
    implementation(libs.hilt.navigation.compose)

    implementation(libs.bundles.lifecycle)

    implementation(libs.google.gms.services.ads)

    implementation(libs.bundles.billing)
}