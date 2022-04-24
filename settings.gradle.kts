pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        mavenCentral()
        google()
        maven(url = uri("https://jitpack.io"))
    }
}

include(":app", ":comvvmhelper")
rootProject.name = "CoMvvmHelper"
