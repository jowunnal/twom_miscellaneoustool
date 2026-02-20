plugins {
    id("jinProject.android.library")
    id("kotlinx-serialization")
}

android {
    namespace = "com.jinproject.features.symbol.api"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.navigation3.runtime)
}
