pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }

    versionCatalogs {
        create("libs") {
            version("androidApplication", "7.0.0")
            version("kotlinAndroid", "1.5.21")
            version("googleServices", "4.3.8")
            version("navigationSafeArgs", "2.8.1")

            plugin("android.application", "com.android.application").versionRef("androidApplication")
            plugin("kotlin.android", "org.jetbrains.kotlin.android").versionRef("kotlinAndroid")
            plugin("google.services", "com.google.gms.google-services").versionRef("googleServices")
            plugin("navigation.safe.args", "androidx.navigation.safeargs.kotlin").versionRef("navigationSafeArgs")
            // Add other dependencies here
        }
    }
}

rootProject.name = "DopeFits"
include(":app")