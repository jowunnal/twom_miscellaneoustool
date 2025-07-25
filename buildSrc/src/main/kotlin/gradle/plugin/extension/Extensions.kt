package gradle.plugin.extension

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.google.protobuf.gradle.ProtobufExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

internal val Project.applicationExtension: CommonExtension<*, *, *, *, *, *>
    get() = extensions.getByType<ApplicationExtension>()

internal val Project.libraryExtension: CommonExtension<*, *, *, *, *, *>
    get() = extensions.getByType<LibraryExtension>()

internal val Project.androidExtension: CommonExtension<*, *, *, *, *, *>
    get() = runCatching { libraryExtension }
        .recoverCatching { applicationExtension }
        .onFailure { println("Could not find Library or Application extension from this project") }
        .getOrThrow()

internal val Project.protobufExtension: ProtobufExtension get() = extensions.getByType<ProtobufExtension>()

internal val Project.libraryAndroidComponentExtension: LibraryAndroidComponentsExtension get() = extensions.getByType<LibraryAndroidComponentsExtension>()

fun Project.getVersionCatalog() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun KotlinProjectExtension.allowExplicitBackingFields() {
    sourceSets.all {
        languageSettings.enableLanguageFeature("ExplicitBackingFields")
    }
}