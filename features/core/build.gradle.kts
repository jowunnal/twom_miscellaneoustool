plugins {
    id("jinProject.android.library")
    id("jinProject.android.hilt")
    id("jinProject.android.compose")
}

android {
    namespace = "com.jinproject.features.core"

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(project(":design"))

    implementation(libs.appcompat)
    implementation(libs.bumptech.glide)
    implementation(libs.coil)
    api(libs.bundles.billing)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.androidx.navigation3.runtime)
    api(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    api(libs.firebase.auth)
    api(libs.firebase.database)
}
