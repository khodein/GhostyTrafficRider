import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.getByType

plugins {
    id("org.jetbrains.kotlin.plugin.serialization")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun DependencyHandler.addKtorDependencies() {
    add("implementation", platform(libs.findLibrary("ktor-client-bom").get()))
    add("implementation", libs.findLibrary("ktor-client-android").get())
    add("implementation", libs.findLibrary("ktor-client-core").get())
    add("implementation", libs.findLibrary("ktor-client-serialization-kotlinx-json").get())
    add("implementation", libs.findLibrary("ktor-client-logging").get())
    add("implementation", libs.findLibrary("ktor-client-content-negotiation").get())
    add("implementation", libs.findLibrary("ktor-client-okhttp").get())
}

pluginManager.withPlugin("com.android.application") {
    dependencies.addKtorDependencies()
}

pluginManager.withPlugin("com.android.library") {
    dependencies.addKtorDependencies()
}
