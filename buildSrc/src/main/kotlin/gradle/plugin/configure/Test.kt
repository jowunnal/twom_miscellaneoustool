package gradle.plugin.configure

import com.android.build.api.dsl.LibraryExtension
import gradle.plugin.extension.getVersionCatalog
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

@Suppress("UnstableApiUsage")
internal fun Project.configureAndroidTest() {
    extensions.configure<LibraryExtension> {
        defaultConfig {
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        testOptions {
            unitTests.all {
                it.useJUnitPlatform()
            }
        }
    }

    val libs = getVersionCatalog()
    dependencies {
        "implementation"(libs.findLibrary("coroutines-core").get())
        "testImplementation"(libs.findLibrary("coroutines-test").get())
        "testImplementation"(libs.findBundle("testing").get())
        "testImplementation"(libs.findBundle("kotest").get())
        "testRuntimeOnly"(libs.findLibrary("junit.jupiter.engine").get())
    }
}