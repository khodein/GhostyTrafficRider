plugins {
    id("ghostytrafficrider.android.library")
    id("ghostytrafficrider.android.core")
    id("ghostytrafficrider.android.compose")
    id("ghostytrafficrider.android.navigation3")
}

android {
    namespace = "com.ghosty.traffic.rider.framework"
}

dependencies {
    implementation(project(":framework:tools"))
}
