import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.getByType

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun DependencyHandler.addCoilDependencies() {
    add("implementation", libs.findLibrary("coil").get())
    add("implementation", libs.findLibrary("coil-network-okhttp").get())
}

pluginManager.withPlugin("com.android.application") {
    dependencies.addCoilDependencies()
}

pluginManager.withPlugin("com.android.library") {
    dependencies.addCoilDependencies()
}
