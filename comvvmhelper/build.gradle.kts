plugins {
    id("com.kuky.apps.library")
    id("maven-publish")
}

group = "com.github.kukyxs"

afterEvaluate {
    publishing {
        publications {
            create("release", MavenPublication::class) {
                from(components.getByName("release"))
                groupId = "com.github.kukyxs"
                artifactId = "CoMvvmHelper"
                version = "0.9.2.1"
            }
        }
    }
}

dependencies {
    api(libs.bundles.appcompact)
    api(libs.bundles.lifecycle)
    api(libs.bundles.uis)
    api(libs.bundles.others)
    api(libs.bundles.koin)
    kapt(libs.bundles.kapts)
}