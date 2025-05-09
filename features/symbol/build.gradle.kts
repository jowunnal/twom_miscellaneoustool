plugins {
    id("jinProject.android.feature")
    id("kotlin-parcelize")
}

android {
    namespace = "com.jinproject.features.symbol"
    lint {
        abortOnError = false
        checkReleaseBuilds = false
    }
}

dependencies {
    implementation(libs.constraintLayout)

    implementation(libs.firebase.storage)
    implementation(libs.airbnb.android.lottie.compose)
}