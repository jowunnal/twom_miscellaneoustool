plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.gradle.android)
    implementation(libs.gradle.kotlin)
    implementation(libs.gradle.kotlin.compose)
    implementation(libs.gradle.hilt)
    implementation(libs.gradle.google.devtools.ksp)
    implementation(libs.gradle.google.gms.google.services)
    implementation(libs.gradle.google.firebase.crashlytics)
    implementation(libs.gradle.androidx.navigation.safe.args)
    implementation(libs.gradle.protobuf)
    implementation(libs.gradle.kotlin.serialization)
    implementation(libs.gradle.room)
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
        register("googleGms") {
            id = "jinProject.android.gms-services"
            implementationClass = "gradle.plugin.google.GoogleGmsPlugin"
        }
        register("googleFirebase") {
            id = "jinProject.android.firebase"
            implementationClass = "gradle.plugin.google.GoogleFirebasePlugin"
        }
        register("androidProtobuf") {
            id = "jinProject.android.protobuf"
            implementationClass = "gradle.plugin.android.AndroidProtobufPlugin"
        }
        register("androidNavigationSafeArgs") {
            id = "jinProject.android.safeArgs"
            implementationClass = "gradle.plugin.android.AndroidxNavigationSafeArgsPlugin"
        }
        register("androidRoom") {
            id = "jinProject.android.room"
            implementationClass = "gradle.plugin.android.AndroidRoomPlugin"
        }
    }
}