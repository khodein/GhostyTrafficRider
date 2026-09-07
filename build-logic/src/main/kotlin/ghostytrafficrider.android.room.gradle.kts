import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.getByType

plugins {
    id("com.google.devtools.ksp")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun DependencyHandler.addRoomDependencies() {
    add("implementation", libs.findLibrary("androidx-room-runtime").get())
    add("implementation", libs.findLibrary("androidx-room-ktx").get())
    add("ksp", libs.findLibrary("androidx-room-compiler").get())
    add("testImplementation", libs.findLibrary("androidx-room-testing").get())
}

pluginManager.withPlugin("com.android.application") {
    dependencies.addRoomDependencies()
}

pluginManager.withPlugin("com.android.library") {
    dependencies.addRoomDependencies()
}
