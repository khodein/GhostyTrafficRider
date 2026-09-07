import com.android.build.api.dsl.LibraryExtension

plugins {
    id("ghostytrafficrider.android.library")
    id("ghostytrafficrider.android.core")
    id("org.jetbrains.kotlin.plugin.compose")
}

pluginManager.withPlugin("com.android.library") {
    extensions.configure<LibraryExtension>("android") {
        buildFeatures {
            compose = true
        }
    }
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun DependencyHandler.addFeatureApiDependencies() {
    add("implementation", project(":framework:tools"))
    add("implementation", platform(libs.findLibrary("androidx-compose-bom").get()))
    add("implementation", libs.findLibrary("androidx-compose-runtime").get())
    add("implementation", libs.findLibrary("androidx-compose-ui").get())
}

pluginManager.withPlugin("com.android.library") {
    dependencies.addFeatureApiDependencies()
}
