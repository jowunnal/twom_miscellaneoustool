plugins {
    id("jinProject.android.feature")
}

android {
    namespace = "com.jinproject.features.home"
}

dependencies {
    implementation(project(":features:alarm:api"))
    implementation(project(":features:collection:api"))
    implementation(project(":features:droplist:api"))
}