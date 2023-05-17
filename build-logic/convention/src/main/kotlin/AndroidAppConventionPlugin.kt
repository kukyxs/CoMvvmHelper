import com.android.build.api.dsl.ApplicationExtension
import com.kuky.apps.buildlogic.configureKotlinAndroid
import com.kuky.apps.buildlogic.configureKotlinAndroidToolchain
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidAppConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("kotlin-android")
                apply("kotlin-kapt")
                apply("dagger.hilt.android.plugin")
                apply("kotlin-parcelize")
            }

            configureKotlinAndroidToolchain()
            extensions.configure<ApplicationExtension> {
                defaultConfig.targetSdk = 33
                configureKotlinAndroid(this)

                buildTypes {
                    release {
                        isMinifyEnabled = true
                        isShrinkResources = true
                        proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
                    }

                    debug {
                        isMinifyEnabled = true
                        isShrinkResources = true
                        proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
                    }
                }
            }
        }
    }
}