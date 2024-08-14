plugins {
    id("jinProject.android.feature")
    id("kotlin-parcelize")
}

android {
    namespace = "com.jinproject.features.symbol"
}

dependencies {
    implementation(libs.constraintLayout)

    implementation(libs.bundles.billing)
}