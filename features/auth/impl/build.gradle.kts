plugins {
    id("jinProject.android.feature")
}

android {
    namespace = "com.jinproject.features.auth"
}

dependencies {
    implementation(project(":features:auth:api"))
    implementation(project(":features:symbol:api"))

    implementation(libs.airbnb.android.lottie.compose)
}