import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.getByType

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun DependencyHandler.addDataStoreDependencies() {
    add("implementation", libs.findLibrary("androidx-datastore-preferences").get())
}

pluginManager.withPlugin("com.android.application") {
    dependencies.addDataStoreDependencies()
}

pluginManager.withPlugin("com.android.library") {
    dependencies.addDataStoreDependencies()
}
