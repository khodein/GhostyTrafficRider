plugins {
    id("ghostytrafficrider.android.library")
    id("ghostytrafficrider.android.core")
}

android {
    namespace = "com.ghosty.traffic.rider.vpn"
}

dependencies {
    implementation(project(":feature-profile:api"))
    implementation(libs.snakeyaml.engine)
    implementation(libs.kotlinx.coroutines.core)
    compileOnly(files("libs/libmihomo-android-v0.3.1.aar"))
}
