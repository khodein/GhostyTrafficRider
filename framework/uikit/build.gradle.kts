plugins {
    id("ghostytrafficrider.android.library")
    id("ghostytrafficrider.android.compose")
}

android {
    namespace = "com.ghosty.traffic.rider.framework.uikit"
}

dependencies {
    implementation(project(":framework"))
}
