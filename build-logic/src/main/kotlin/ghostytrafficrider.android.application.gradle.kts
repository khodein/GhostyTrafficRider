import com.android.build.api.dsl.ApplicationExtension

plugins {
    id("com.android.application")
}

private val major = providers.gradleProperty("app.version.major").get().toInt()
private val minor = providers.gradleProperty("app.version.minor").get().toInt()
private val patch = providers.gradleProperty("app.version.patch").get().toInt()

private val name: String
    get() = "$major.$minor.$patch"
private val code: Int
    get() = providers.gradleProperty("app.version.code").get().toInt()

val isReleaseMinifyEnabled = providers.gradleProperty("app.release.minify")
    .map(String::toBoolean)
    .getOrElse(false)

extensions.configure<ApplicationExtension>("android") {
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        minSdk = 28
        targetSdk = 37
        versionCode = code
        versionName = name

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = isReleaseMinifyEnabled
            isShrinkResources = isReleaseMinifyEnabled
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            isDebuggable = true
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
