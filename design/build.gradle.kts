plugins {
    id("jinProject.android.library")
    id("jinProject.android.compose")
}

android {
    namespace = "com.jinproject.design_ui"
}

dependencies {
    implementation(project(":core"))

    implementation(libs.bumptech.glide)
    implementation(libs.coil)
    implementation(libs.airbnb.android.lottie.compose)
}
