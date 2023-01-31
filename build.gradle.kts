// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    apply(from = "configs.gradle.kts")
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0")
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
