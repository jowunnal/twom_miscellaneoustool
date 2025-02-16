plugins {
    id("jinProject.android.library")
    id("jinProject.android.compose")
}

android {
    namespace = "com.jinproject.design_compose"
}

dependencies {
    implementation(project(":design-ui"))
    implementation(project(":core"))

    implementation(libs.bumptech.glide)
    implementation(libs.coil)
    implementation(libs.airbnb.android.lottie.compose)
}