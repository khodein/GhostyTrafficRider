plugins {
    id("ghostytrafficrider.android.application")
    id("ghostytrafficrider.android.core")
    id("ghostytrafficrider.android.compose")
    id("ghostytrafficrider.android.navigation3")
    id("ghostytrafficrider.koin")
    id("ghostytrafficrider.android.room")
    id("ghostytrafficrider.android.test")
}

android {
    namespace = "com.ghosty.traffic.rider"

    packaging {
        jniLibs.useLegacyPackaging = true
    }

    defaultConfig {
        applicationId = "com.ghosty.traffic.rider"
    }
}

dependencies {
    implementation(project(":framework"))
    implementation(project(":framework:tools"))
    implementation(project(":framework:router"))
    implementation(files("../feature-vpn/libs/libmihomo-android-v0.3.1.aar"))
    implementationFeatureModules()
}

fun DependencyHandlerScope.implementationFeatureModules() {
    rootProject.subprojects
        .asSequence()
        .filter { project ->
            val isFeatureSubmodule = (project.name == "impl" || project.name == "api") &&
                project.parent?.name?.startsWith("feature-") == true
            val isLegacyFeature = project.name.startsWith("feature-") && project.childProjects.isEmpty()
            isFeatureSubmodule || isLegacyFeature
        }
        .sortedBy { it.path }
        .forEach { implementation(project(it.path)) }
}
