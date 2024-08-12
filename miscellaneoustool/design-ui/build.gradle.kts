plugins {
    id("jinProject.android.library")
}

android {
    namespace = "com.jinproject.design_ui"

    buildFeatures{
        dataBinding = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
}