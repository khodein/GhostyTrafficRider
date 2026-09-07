plugins {
    id("ghostytrafficrider.android.feature")
}

android {
    namespace = "com.ghosty.traffic.rider.feature.profile.impl"
}

dependencies {
    implementation(libs.snakeyaml.engine)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.core)
    implementation(project(":feature-profile:api"))
}
