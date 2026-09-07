import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.getByType

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun DependencyHandler.addKoinDependencies() {
    add("implementation", libs.findLibrary("koin-core").get())
    add("implementation", libs.findLibrary("koin-android").get())
    add("implementation", libs.findLibrary("koin-androidx-compose").get())
}

pluginManager.withPlugin("com.android.application") {
    dependencies.addKoinDependencies()
}

pluginManager.withPlugin("com.android.library") {
    dependencies.addKoinDependencies()
}
