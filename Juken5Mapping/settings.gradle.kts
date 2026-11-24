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
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Juken 5 Mapping"
include(":app")
include(":core-domain")
include(":core-ui")
include(":core-protocol")
include(":core-sync")
include(":core-state")
include(":core-data")
include(":core-telemetry")
include(":core-analysis")
include(":core-autotune")
include(":core-workspace")
include(":core-charts")
include(":core-transport")
include(":core-transport-bluetooth")
include(":core-common")
include(":feature-dashboard")
include(":feature-mapping")
include(":feature-datalogger")
include(":feature-autotune")
include(":feature-autotune-workspace")
include(":feature-reports")
include(":feature-connection")
include(":feature-settings")
include(":feature-protocol-inspector")
include(":feature-hardware-validation")
include(":mock-ecu")
