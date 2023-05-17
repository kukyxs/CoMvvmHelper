plugins {
   id("com.kuky.apps.app")
}

android {
    compileSdk = 33

    defaultConfig {
        applicationId = "com.kuky.comvvmhelper"
        minSdk = 21
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    kapt {
        correctErrorTypes = true
    }

    lint {
        baseline = file("lint-baseline.xml")
    }
}

dependencies {
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.android.cardview)
    implementation(libs.glide)
    kapt(libs.glide.compiler)
    implementation(libs.coil)

    implementation(project(path = ":comvvmhelper"))
}