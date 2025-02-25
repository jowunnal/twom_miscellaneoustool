plugins {
    id("jinProject.android.feature")
}

android {
    namespace = "com.jinproject.features.home"
    compileSdk = 34
}

dependencies {
    implementation(project(":features:collection"))
    implementation(project(":features:droplist"))
}