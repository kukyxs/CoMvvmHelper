// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    apply(from = "configs.gradle.kts")
}

plugins {
    id("com.android.application").version("7.3.1") apply false
    id("com.android.library").version("7.3.1") apply false
    id("org.jetbrains.kotlin.android").version("1.7.10") apply false
}

task("clean", type = Delete::class) {
    delete(rootProject.buildDir)
}
