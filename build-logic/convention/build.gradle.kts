import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "com.kuky.apps.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.koltin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidLibrary") {
            id = "com.kuky.apps.library"
            implementationClass = "AndroidLibConventionPlugin"
        }

        register("androidApp") {
            id = "com.kuky.apps.app"
            implementationClass = "AndroidAppConventionPlugin"
        }
    }
}