plugins {
    id("jinProject.android.feature")
}

android {
    namespace = "com.jinproject.features.home"

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(libs.constraintLayout)
}