plugins {
    id("ghostytrafficrider.android.feature")
}

android {
    namespace = "com.ghosty.traffic.rider.feature.routing.impl"
}

dependencies {
    implementation(project(":feature-routing:api"))
}
