plugins {
    id("ghostytrafficrider.android.feature.api")
}

android {
    namespace = "com.ghosty.traffic.rider.feature.vpn.api"
}

dependencies {
    implementation(project(":feature-selfprofile:api"))
}
