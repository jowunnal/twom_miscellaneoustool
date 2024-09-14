package gradle.plugin.android

import gradle.plugin.extension.getVersionCatalog
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidRoomPlugin: Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("androidx.room")
            apply("com.google.devtools.ksp")
        }

        val libs = getVersionCatalog()

        dependencies {
            "implementation"(libs.findLibrary("room.runtime").get())
            "annotationProcessor"(libs.findLibrary("room.compiler").get())
            "implementation"(libs.findLibrary("room.ktx").get())
            "implementation"(libs.findLibrary("room.common").get())
            "ksp"(libs.findLibrary("room.compiler").get())
            "androidTestImplementation"(libs.findLibrary("room.testing").get())
        }
    }

}