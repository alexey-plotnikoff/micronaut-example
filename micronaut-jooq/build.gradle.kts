import org.jooq.meta.jaxb.Logging

plugins {
    id("java-library")
    id("nu.studer.jooq") version "8.1"
    id("org.flywaydb.flyway") version "9.15.2"
}

val dbDriver = "org.postgresql.Driver"
val dbUrl = "jdbc:postgresql://localhost:5432/micronaut-example"
val dbUsername = "postgres"
val dbPassword = "mysecretpassword"

jooq {
    version.set(jooqVersion)
    edition.set(nu.studer.gradle.jooq.JooqEdition.OSS)  // the default (can be omitted)
    configurations {
        create("main") {
            generateSchemaSourceOnCompilation.set(true)

            jooqConfiguration.apply {
                logging = Logging.WARN
                jdbc.apply {
                    driver = dbDriver
                    url = dbUrl
                    user = dbUsername
                    password = dbPassword
                }
                generator.apply {
                    name = "org.jooq.codegen.DefaultGenerator"
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                        excludes = listOf(
                            "flyway_schema_history|pg_stat_statements|shedlock"
                        ).joinToString("|")
                        recordVersionFields = "version"
                        recordTimestampFields = "last_modified_date"
                    }
                    generate.apply {
                        isDeprecated = false
                        isRecords = true
                        isImmutablePojos = true
                        isFluentSetters = true
                        isNonnullAnnotation = true
                        isNullableAnnotation = true
                        isJpaAnnotations = true // For GraalVM - https://micronaut-projects.github.io/micronaut-sql/latest/guide/index.html#jooq-graalvm
                        nonnullAnnotationType = "org.jetbrains.annotations.NotNull"
                        nullableAnnotationType = "org.jetbrains.annotations.Nullable"
                    }
                    target.apply {
                        packageName = "am.plotnikov.example.jooq"
                        directory = "build/generated-src/jooq/main"  // default (can be omitted)
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}

flyway {
    driver = dbDriver
    url = dbUrl
    user = dbUsername
    password = dbPassword
    locations = arrayOf("classpath:db/migration")
    validateMigrationNaming = true
}

dependencies {
    jooqGenerator("org.postgresql:postgresql:$postgresqlVersion")
    api("org.postgresql:postgresql:$postgresqlVersion")
    api("org.flywaydb:flyway-core:$flywayVersion")
    // For GraalVM - https://micronaut-projects.github.io/micronaut-sql/latest/guide/index.html#jooq-graalvm
    implementation("org.simpleflatmapper:sfm-jdbc:8.2.3")
    // for JPA annotations
    api("javax.persistence:javax.persistence-api:2.2")

    api("org.jetbrains:annotations:23.0.0")
}
