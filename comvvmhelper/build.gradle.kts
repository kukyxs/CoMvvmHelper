@file:Suppress("UNCHECKED_CAST")

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("maven-publish")
}

group = "com.github.kukyxs"

android {
    compileSdk = 31
    buildToolsVersion = "30.0.3"

    defaultConfig {
        minSdk = 21
        targetSdk = 31
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    buildFeatures.dataBinding = true
}

afterEvaluate {
    publishing {
        publications {
            create("release", MavenPublication::class) {
                from(components.getByName("release"))
                groupId = "com.github.kukyxs"
                artifactId = "CoMvvmHelper"
                version = "0.7.6-x"
            }
        }
    }
}

dependencies {
    implementation(fileTree("dir" to "libs", "include" to listOf("*.jar")))

    val imps = rootProject.extra.get("imps") as Map<String, String>
    if (imps.isNotEmpty()) {
        imps.values.forEach { implementation(it) }
    }

    val kapts = rootProject.extra.get("kapts") as Map<String, String>
    if (kapts.isNotEmpty()) {
        kapts.values.forEach { kapt(it) }
    }

    val apis = rootProject.extra.get("apis") as Map<String, String>
    if (apis.isNotEmpty()) {
        apis.values.forEach { api(it) }
    }
}