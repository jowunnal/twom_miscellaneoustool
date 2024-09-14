plugins {
    id("jinProject.android.application")
    id("jinProject.android.safeArgs")
}

android {
    namespace = "com.jinproject.twomillustratedbook"

    defaultConfig {
        applicationId = "com.jinproject.twomillustratedbook"
        targetSdk = 34
        versionCode = 49
        versionName = "2.3.0"
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
    implementation(project(":features:core"))
    implementation(project(":features:symbol"))
    implementation(project(":features:simulator"))
    implementation(project(":design-compose"))
    implementation(project(":design-ui"))

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity.compose)

    implementation(libs.bundles.navigation)
    implementation(libs.hilt.navigation.compose)

    implementation(libs.bundles.lifecycle)

    implementation(libs.bundles.billing)
    implementation(libs.bundles.playInAppUpdate)
}