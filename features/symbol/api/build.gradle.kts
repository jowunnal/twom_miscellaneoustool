plugins {
    id("jinProject.android.library")
    id("kotlinx-serialization")
}

android {
    namespace = "com.jinproject.features.symbol.api"
}

dependencies {
    implementation(project(":features:core"))
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.navigation3.runtime)
}
