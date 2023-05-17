import com.android.build.gradle.LibraryExtension
import com.kuky.apps.buildlogic.configureKotlinAndroid
import com.kuky.apps.buildlogic.configureKotlinAndroidToolchain
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidLibConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("kotlin-android")
                apply("kotlin-kapt")
            }

            configureKotlinAndroidToolchain()
            extensions.configure<LibraryExtension> {
                defaultConfig.targetSdk = 33
                configureKotlinAndroid(this)
            }

            dependencies {
                add("androidTestImplementation", kotlin("test"))
                add("testImplementation", kotlin("test"))
            }
        }
    }
}