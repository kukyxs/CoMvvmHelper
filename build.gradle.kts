// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    apply(from = "configs.gradle.kts")
}

plugins {
    id("com.android.application").version("7.4.2") apply false
    id("com.android.library").version("7.4.2") apply false
    id("org.jetbrains.kotlin.android").version("1.8.20") apply false
}
