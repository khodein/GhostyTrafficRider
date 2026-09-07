import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.getByType

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun DependencyHandler.addNavigation3Dependencies() {
    add("implementation", libs.findLibrary("androidx-navigation3-ui").get())
    add("implementation", libs.findLibrary("androidx-navigation3-runtime").get())
    add("implementation", libs.findLibrary("androidx-lifecycle-viewmodel-navigation3").get())
    add("implementation", libs.findLibrary("androidx-material3-adaptive-navigation3").get())
}

pluginManager.withPlugin("com.android.application") {
    dependencies.addNavigation3Dependencies()
}

pluginManager.withPlugin("com.android.library") {
    dependencies.addNavigation3Dependencies()
}
