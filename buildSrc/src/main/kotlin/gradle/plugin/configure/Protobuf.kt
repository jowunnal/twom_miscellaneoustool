package gradle.plugin.configure

import gradle.plugin.extension.protobufExtension
import org.gradle.api.Project

internal fun Project.configureProtobuf() {
    protobufExtension.apply {
        protoc {
            artifact = "com.google.protobuf:protoc:3.25.0"
        }

        generateProtoTasks {
            all().forEach { task ->
                task.builtins {
                    create("java") {
                        option("lite")
                    }
                }
            }
        }
    }

    /*libraryAndroidComponentExtension.apply {
        onVariants(selector().all()) { variant ->
            afterEvaluate {
                val protoTask =
                    project.tasks.getByName("generate" + variant.name.replaceFirstChar { it.uppercaseChar() } + "Proto") as GenerateProtoTask

                project.tasks.getByName("ksp" + variant.name.replaceFirstChar { it.uppercaseChar() } + "Kotlin") {
                    dependsOn(protoTask)
                    (this as org.jetbrains.kotlin.gradle.tasks.AbstractKotlinCompileTool<*>).setSource(
                        protoTask.outputBaseDir
                    )
                }
            }
        }
    }*/
}