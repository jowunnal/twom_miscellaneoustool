package gradle.plugin.kotlin

import gradle.plugin.kotlin.configure.configureKotlinJVM
import gradle.plugin.kotlin.configure.configureKotlinTest
import org.gradle.api.Plugin
import org.gradle.api.Project

class KotlinLibraryPlugin: Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("org.jetbrains.kotlin.jvm")

            configureKotlinJVM()
            configureKotlinTest()
        }
    }
}