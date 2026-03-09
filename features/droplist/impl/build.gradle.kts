plugins {
    id("jinProject.android.feature")
}

android {
    namespace = "com.jinproject.features.droplist"
}

dependencies {
    implementation(project(":features:droplist:api"))
    implementation(libs.core.ktx)
    implementation(libs.material)
    implementation(libs.constraintLayout)
}