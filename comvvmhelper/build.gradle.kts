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
                version = "0.9.1.3-x"
            }
        }
    }
}

dependencies {
    api(libs.bundles.appcompact)
    api(libs.bundles.lifecycle)
    api(libs.bundles.uis)
    api(libs.bundles.others)
    kapt(libs.bundles.kapts)
}