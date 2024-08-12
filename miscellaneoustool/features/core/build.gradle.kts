plugins {
    id("jinProject.android.feature")
}

android {
    namespace = "com.jinproject.features.core"

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)

    implementation(libs.bundles.billing)
}