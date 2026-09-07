import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.getByType

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun DependencyHandler.addCoreDependencies() {
    add("implementation", libs.findLibrary("androidx-appcompat").get())
    add("implementation", libs.findLibrary("androidx-core-ktx").get())
    add("implementation", libs.findLibrary("androidx-lifecycle-runtime-ktx").get())
    add("implementation", libs.findLibrary("androidx-lifecycle-viewmodel-ktx").get())
}

pluginManager.withPlugin("com.android.application") {
    dependencies.addCoreDependencies()
}

pluginManager.withPlugin("com.android.library") {
    dependencies.addCoreDependencies()
}
