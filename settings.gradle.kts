rootProject.name = "pulserank"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

pluginManagement {
    includeBuild("build-logic")

    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

include(
    "platform:common",
    "platform:event-model",
    "services:product-service",
    "services:event-service",
    "services:query-service",
    "services:event-generator",
    "streaming:flink-job"
)