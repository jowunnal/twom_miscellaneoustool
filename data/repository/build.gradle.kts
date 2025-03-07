plugins {
    id("jinProject.android.library")
    id("jinProject.android.hilt")
}

android {
    namespace = "com.jinproject.data.repository"
    compileSdk = 35

}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core"))

    implementation(libs.coroutines.core)
    api(libs.datastore)
}