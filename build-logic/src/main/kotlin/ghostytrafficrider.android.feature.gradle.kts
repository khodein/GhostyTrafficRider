import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.getByType

plugins {
    id("ghostytrafficrider.android.library")
    id("ghostytrafficrider.android.core")
    id("ghostytrafficrider.android.compose")
    id("ghostytrafficrider.koin")
    id("ghostytrafficrider.ktor")
    id("ghostytrafficrider.coil")
    id("ghostytrafficrider.android.room")
    id("ghostytrafficrider.android.datastore")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun DependencyHandler.addFeatureDependencies() {
    add("implementation", libs.findLibrary("androidx-navigation3-runtime").get())
    add("implementation", project(":framework:router"))
    add("implementation", project(":framework"))
    add("implementation", project(":framework:tools"))
}

pluginManager.withPlugin("com.android.library") {
    dependencies.addFeatureDependencies()
}
