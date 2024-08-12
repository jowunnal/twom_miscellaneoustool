plugins {
    id("jinProject.android.feature")
}

android {
    namespace = "com.jinproject.features.gear"
    compileSdk = 34
}

dependencies {
    implementation(libs.bundles.billing)

    // compose NumberPicker
    implementation("com.chargemap.compose:numberpicker:1.0.3")
}