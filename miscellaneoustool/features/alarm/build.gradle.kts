plugins {
    id("jinProject.android.feature")
    id("jinProject.android.parcelize")
}

android {
    namespace = "com.jinproject.features.alarm"
    compileSdk = 34
}

dependencies {
    // compose NumberPicker
    implementation("com.chargemap.compose:numberpicker:1.0.3")
}