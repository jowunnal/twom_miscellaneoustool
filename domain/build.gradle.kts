plugins {
    id("jinProject.android.library")
}

android {
    namespace = "com.jinproject.domain"

    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
}

dependencies {
    implementation(project(":core"))

    implementation(libs.kotlin.reflect)
    implementation(libs.bundles.coroutines)
    implementation(libs.dagger)
    implementation(libs.protobuf)

    testImplementation(libs.bundles.kotest)
    testImplementation(libs.dagger.hilt.android.test)

    testImplementation(libs.bundles.testing)

    testRuntimeOnly(libs.junit.jupiter.engine)

    //implementation 'org.slf4j:slf4j-api:2.0.5'
    //implementation 'ch.qos.logback:logback-classic:1.3.1'
}