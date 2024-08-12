plugins {
    id("jinProject.android.feature")
    id("jinProject.android.parcelize")
}

android {
    namespace = "com.jinproject.features.symbol"
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.constraintLayout)

    implementation(libs.bundles.billing)
}