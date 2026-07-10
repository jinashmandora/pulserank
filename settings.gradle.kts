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
    "common:event-schema",
    "services:event-service",
    "services:query-service",
    "streaming:ranking-job"
)