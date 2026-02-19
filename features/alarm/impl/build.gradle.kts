plugins {
    id("jinProject.android.feature")
    kotlin("plugin.parcelize")
}

android {
    namespace = "com.jinproject.features.alarm"
}

dependencies {
    implementation(project(":features:alarm:api"))

    implementation(libs.billing.ktx)
    implementation(libs.constraintLayout)
    implementation(libs.appcompat)

    // compose NumberPicker
    implementation("com.chargemap.compose:numberpicker:1.0.3")
}