plugins {
    `java-library`
    alias(libs.plugins.avro)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

dependencies {
    api(libs.avro)
}

avro {
    sourceDirectory = "src/main/avro"
    outputDirectory = "generated-sources/avro"
    enableDecimalLogicalType.set(true)
    createSetters.set(true)
}