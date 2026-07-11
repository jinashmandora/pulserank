plugins {
    `java-library`
    alias(libs.plugins.avro)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

dependencies {
    api(libs.avro)

    testImplementation(platform("org.junit:junit-bom:5.13.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

avro {
    sourceDirectory = "src/main/avro"
    outputDirectory = "generated-sources/avro"
    createSetters.set(true)
}

tasks.test {
    useJUnitPlatform()
}