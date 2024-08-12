plugins {
    id("jinProject.android.library")
    id("jinProject.android.protobuf")
}

android {
    namespace = "com.jinproject.core"
}

dependencies {
    implementation(libs.datastore)
}