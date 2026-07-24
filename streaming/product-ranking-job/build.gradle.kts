plugins {
    java
    application
    id("com.gradleup.shadow") version "9.5.1"
    alias(libs.plugins.flyway)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

buildscript {
    dependencies {
        classpath(libs.clickhouse.jdbc)
        classpath(libs.flyway.database.clickhouse)
    }
}


dependencies {

    // Shared Avro models
    implementation(project(":common"))


    // ---------- Flink ----------
    compileOnly("org.apache.flink:flink-streaming-java:2.3.0")
    compileOnly("org.apache.flink:flink-clients:2.3.0")
    compileOnly("org.apache.flink:flink-connector-base:2.3.0")
    compileOnly("org.apache.flink:flink-statebackend-rocksdb:2.3.0")
    implementation("org.apache.flink:flink-avro:2.3.0")
    implementation("org.apache.flink:flink-connector-kafka:5.0.0-2.2")
    implementation(variantOf(libs.flink.connector.clickhouse) {
        classifier("all")
    })

    // ---------- Avro ----------
    implementation("org.apache.avro:avro:1.12.1")
    implementation("io.apicurio:apicurio-registry-avro-serde-kafka:3.2.6")

    // ---------- Logging ----------
    implementation("org.slf4j:slf4j-api:2.0.17")
    runtimeOnly("ch.qos.logback:logback-classic:1.5.18")


    // ---------- Testing ----------
    testImplementation(platform("org.junit:junit-bom:5.13.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

flyway {
    url = "jdbc:clickhouse://localhost:8123/pulserank"
    user = "default"
    password = ""
}

tasks.shadowJar {
    archiveClassifier.set("")
    duplicatesStrategy = DuplicatesStrategy.WARN
    mergeServiceFiles()
    exclude("org/slf4j/impl/**")
    exclude("META-INF/maven/**")

    manifest {
        attributes["Main-Class"] = "com.pulserank.ranking.Application"
    }
}

tasks.test {
    useJUnitPlatform()
}