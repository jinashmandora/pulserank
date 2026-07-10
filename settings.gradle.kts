rootProject.name = "pulserank"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}


dependencyResolutionManagement {
    repositoriesMode.set(
        RepositoriesMode.FAIL_ON_PROJECT_REPOS
    )

    repositories {
        mavenCentral()
    }
}

include(
    "common",
    "services:product-catalog-service",
    "services:event-service",
    "streaming:product-ranking-job"
)