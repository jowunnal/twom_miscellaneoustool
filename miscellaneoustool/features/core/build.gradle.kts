plugins {
    id("jinProject.android.library")
    id("jinProject.android.hilt")
    id("jinProject.android.compose")
    id("kotlin-kapt")
}

android {
    namespace = "com.jinproject.features.core"

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core"))
    implementation(project(":design-ui"))
    implementation(project(":design-compose"))

    implementation(libs.appcompat)
    implementation(libs.bumptech.glide)
    api(libs.bundles.billing)
    implementation(libs.kotlinx.collections.immutable)
    api(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    api(libs.firebase.auth)
    api(libs.firebase.database)
}