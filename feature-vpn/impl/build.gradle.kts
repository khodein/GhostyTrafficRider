plugins {
    id("ghostytrafficrider.android.feature")
}

android {
    namespace = "com.ghosty.traffic.rider.feature.vpn.impl"
}

dependencies {
    implementation(project(":feature-vpn:api"))
    implementation(project(":feature-selfprofile:api"))
    implementation(libs.snakeyaml.engine)
    implementation(libs.kotlinx.coroutines.core)
    compileOnly(files("../libs/libmihomo-android-v0.3.1.aar"))
}
