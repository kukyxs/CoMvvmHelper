// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    apply(from = "configs.gradle.kts")
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.40.5")
    }
}

plugins {
    id("com.android.application").version("7.1.3") apply false
    id("com.android.library").version("7.1.3") apply false
    id("org.jetbrains.kotlin.android").version("1.6.21") apply false
}

task("clean", type = Delete::class) {
    delete(rootProject.buildDir)
}
