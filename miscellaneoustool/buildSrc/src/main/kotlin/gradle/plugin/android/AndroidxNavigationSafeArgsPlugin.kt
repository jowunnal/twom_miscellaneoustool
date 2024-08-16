package gradle.plugin.android

import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidxNavigationSafeArgsPlugin: Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("androidx.navigation.safeargs.kotlin")
        }
    }

}