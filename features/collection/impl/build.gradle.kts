plugins {
    id("jinProject.android.feature")
}

android {
    namespace = "com.jinproject.features.collection"

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(project(":features:collection:api"))
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
}