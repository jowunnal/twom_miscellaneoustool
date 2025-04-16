package gradle.plugin.kotlin.configure

import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.withType

internal fun Project.configureKotlinTest() {
    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }
}