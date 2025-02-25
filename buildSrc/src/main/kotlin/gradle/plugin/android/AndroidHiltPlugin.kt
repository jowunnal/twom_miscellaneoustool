package gradle.plugin.android


import gradle.plugin.extension.getVersionCatalog
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies


internal class AndroidHiltPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        val libs = getVersionCatalog()

        with(pluginManager) {
            apply("com.google.dagger.hilt.android")
            apply("com.google.devtools.ksp")
        }

        dependencies {
            "implementation"(libs.findLibrary("dagger.hilt.android").get())
            "ksp"(libs.findLibrary("dagger.hilt.android.compiler").get())
        }
    }
}
