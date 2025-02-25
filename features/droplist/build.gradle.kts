plugins {
    id("jinProject.android.feature")
}

android {
    namespace = "com.jinproject.features.droplist"

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.material)
    implementation(libs.constraintLayout)
}