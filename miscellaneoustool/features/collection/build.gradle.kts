plugins {
    id("jinProject.android.feature")
    alias(libs.plugins.android.navigation.safeargs.kotlin)
}

android {
    namespace = "com.jinproject.features.collection"

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
}