plugins {
    id("jinProject.android.library")
    id("jinProject.android.compose")
}

android {
    namespace = "com.jinproject.design_compose"
}

dependencies {
    implementation(project(":design-ui"))

    implementation(libs.bumptech.glide)
    implementation(libs.coil)
}