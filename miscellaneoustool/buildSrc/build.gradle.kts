plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.gradle.android)
    implementation(libs.gradle.kotlin)
    implementation(libs.gradle.hilt)
    implementation(libs.gradle.google.devtools.ksp)
    implementation(libs.gradle.google.gms.google.services)
    implementation(libs.gradle.google.firebase.crashlytics)
    implementation(libs.gradle.protobuf)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "jinProject.android.application"
            implementationClass = "gradle.plugin.android.AndroidApplicationPlugin"
        }
        register("androidLibrary") {
            id = "jinProject.android.library"
            implementationClass = "gradle.plugin.android.AndroidLibraryPlugin"
        }
        register("androidHilt") {
            id = "jinProject.android.hilt"
            implementationClass = "gradle.plugin.android.AndroidHiltPlugin"
        }
        register("androidCompose") {
            id = "jinProject.android.compose"
            implementationClass = "gradle.plugin.android.AndroidComposePlugin"
        }
        register("androidFeature") {
            id = "jinProject.android.feature"
            implementationClass = "gradle.plugin.android.AndroidFeaturePlugin"
        }
        register("androidParcelize") {
            id = "jinProject.android.parcelize"
            implementationClass = "gradle.plugin.android.AndroidParcelizePlugin"
        }
        register("androidGmsService") {
            id = "jinProject.android.gms-services"
            implementationClass = "gradle.plugin.android.AndroidGmsServicePlugin"
        }
        register("androidProtobuf") {
            id = "jinProject.android.protobuf"
            implementationClass = "gradle.plugin.android.AndroidProtobufPlugin"
        }
    }
}