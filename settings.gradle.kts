rootProject.name = "pulserank"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include(
    "common",
    "event-model",
    "product-service",
    "event-service",
    "query-service",
    "event-generator",
    "flink-job"
)