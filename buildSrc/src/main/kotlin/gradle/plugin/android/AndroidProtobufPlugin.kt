package gradle.plugin.android

import com.android.build.api.dsl.LibraryExtension
import gradle.plugin.configure.configureProtobuf
import gradle.plugin.extension.getVersionCatalog
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidProtobufPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("com.google.devtools.ksp")
            apply("com.google.protobuf")
        }

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

        configureProtobuf()

        val libs = getVersionCatalog()
        dependencies {
            "implementation"(libs.findLibrary("protobuf").get())
        }
    }
}