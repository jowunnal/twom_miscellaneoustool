plugins {
    id("jinProject.android.feature")
}

android {
    namespace = "com.jinproject.features.home"
}

dependencies {
    implementation(project(":features:collection"))
    implementation(project(":features:droplist"))
}