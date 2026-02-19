plugins {
    id("jinProject.android.feature")
}

android {
    namespace = "com.jinproject.features.info"
}

dependencies {
    implementation(project(":features:auth:api"))
}