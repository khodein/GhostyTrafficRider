import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.getByType

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun DependencyHandler.addAndroidTestDependencies() {
    add("testImplementation", libs.findLibrary("junit").get())
    add("androidTestImplementation", platform(libs.findLibrary("androidx-compose-bom").get()))
    add("androidTestImplementation", libs.findLibrary("androidx-compose-ui-test-junit4").get())
    add("androidTestImplementation", libs.findLibrary("androidx-espresso-core").get())
    add("androidTestImplementation", libs.findLibrary("androidx-junit").get())
    add("debugImplementation", libs.findLibrary("androidx-compose-ui-test-manifest").get())
    add("debugImplementation", libs.findLibrary("androidx-compose-ui-tooling").get())
}

pluginManager.withPlugin("com.android.application") {
    dependencies.addAndroidTestDependencies()
}

pluginManager.withPlugin("com.android.library") {
    dependencies.addAndroidTestDependencies()
}
