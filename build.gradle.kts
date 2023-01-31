// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    apply(from = "configs.gradle.kts")
    val kotlin_version by extra("1.8.0")
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.44.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    }
    repositories {
        mavenCentral()
    }
}

plugins {
    id("com.android.application").version("7.4.0") apply false
    id("com.android.library").version("7.4.0") apply false
    id("org.jetbrains.kotlin.android").version("1.8.0") apply false
}

task("clean", type = Delete::class) {
    delete(rootProject.buildDir)
}
