import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.apache.tools.ant.taskdefs.condition.Os

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.micronaut.application") version "3.7.4"
    id("io.spring.dependency-management") version "1.1.0"
}

// Fix M1 arm64 exception - https://github.com/bmuschko/gradle-docker-plugin/issues/1035
buildscript {
    dependencies {
        classpath("com.github.docker-java:docker-java:$dockerJavaVersion")
        classpath("com.github.docker-java:docker-java-transport-httpclient5:$dockerJavaVersion")
    }
}

dependencies {
    implementation(project(":micronaut-jooq"))
    implementation(project(":micronaut-grpc"))

    implementation("io.micronaut:micronaut-jackson-databind")
    implementation("io.micronaut.flyway:micronaut-flyway")
    runtimeOnly("io.micronaut.sql:micronaut-jdbc-hikari")
    implementation("io.micronaut.sql:micronaut-jooq")
    implementation("jakarta.annotation:jakarta.annotation-api")
    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("org.postgresql:postgresql")
    implementation("io.micronaut:micronaut-validation")
    implementation("io.micronaut.grpc:micronaut-grpc-runtime")
    annotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")
    implementation("org.mapstruct:mapstruct:$mapstructVersion")

    implementation("io.micronaut.micrometer:micronaut-micrometer-registry-prometheus")
    implementation("me.dinowernli:java-grpc-prometheus:0.6.0")

    implementation("io.github.resilience4j:resilience4j-micronaut:2.0.2")

    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:testcontainers")
    testImplementation("io.micronaut:micronaut-http-client")
}

tasks.withType<ShadowJar> {
    archiveBaseName.set("app")
    archiveClassifier.set("")
}

application {
    mainClass.set("am.plotnikov.example.Application")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

graalvmNative.toolchainDetection.set(false)

tasks.named<io.micronaut.gradle.docker.NativeImageDockerfile>("dockerfileNative") {
    baseImage("gcr.io/distroless/cc-debian10")
    graalArch.set(if (Os.isArch("aarch64")) "aarch64" else "amd64")
}

micronaut {
//    TODO: row below doesn't work, so use gradle.properties
//    version(micronautVersion)
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("am.plotnikov.*")
    }
}
