plugins {
    id("jinProject.android.feature")
    kotlin("plugin.parcelize")
}

android {
    namespace = "com.jinproject.features.alarm"
}

dependencies {
    implementation(libs.billing.ktx)
    implementation(libs.navigation.fragment.compose)

    // compose NumberPicker
    implementation("com.chargemap.compose:numberpicker:1.0.3")
}