import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion

plugins {
    id("com.android.library")
}

extensions.configure<LibraryExtension>("android") {
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
