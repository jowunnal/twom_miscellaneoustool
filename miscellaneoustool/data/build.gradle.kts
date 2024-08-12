import com.google.protobuf.gradle.GenerateProtoTask

plugins {
    id("jinProject.android.library")
    id("jinProject.android.hilt")
    id("jinProject.android.protobuf")
}

android {
    namespace = "com.jinproject.data"

    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
    sourceSets {
        getByName("androidTest").assets.srcDir("$projectDir/schemas")
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core"))

    testImplementation(libs.jUnit)
    androidTestImplementation(libs.test.ext)
    androidTestImplementation(libs.test.espresso)

    implementation(libs.coroutines.core)

    implementation(libs.datastore)

    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    androidTestImplementation(libs.room.testing)
}

androidComponents {
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
}