plugins {
    id("jinProject.kotlin.library")
}

dependencies {
    implementation(libs.kotlin.reflect)
    implementation(libs.bundles.coroutines)
    implementation(libs.dagger)

    testImplementation(libs.bundles.kotest)
    testImplementation(libs.bundles.testing)
    testRuntimeOnly(libs.junit.jupiter.engine)

    //implementation 'org.slf4j:slf4j-api:2.0.5'
    //implementation 'ch.qos.logback:logback-classic:1.3.1'
}