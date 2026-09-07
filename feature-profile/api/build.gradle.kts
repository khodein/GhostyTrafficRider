plugins {
    id("ghostytrafficrider.android.feature.api")
}

android {
    namespace = "com.ghosty.traffic.rider.feature.profile.api"
}

dependencies {
    api(libs.androidx.navigation3.runtime)
    api(libs.kotlinx.coroutines.core)
}
