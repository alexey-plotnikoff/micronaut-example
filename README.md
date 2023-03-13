# Description
This is a microservice template project based on [Micronaut](https://micronaut.io/), Java 17 and Gradle Kotlin DSL.

# Projects
1. ```micronaut-jooq``` - subproject contains jOOQ generated classes and Flyway migrations
2. ```micronaut-grpc``` - subproject describes gRPC contract
3. ```micronaut-app``` - main service that is ready to receive incoming requests

# micronaut-jooq
Follow next steps:
1. First of all we need start PostgreSQL(via Docker for example) on localhost and port 5432.
2. Create database "micronaut-example"(should rename to real microservice name).
3. Execute Gradle tasks to apply flyway DB migrations(micronaut-jooq/src/main/resources/db/migration):
```
:micronaut-jooq:assemble
:micronaut-jooq:flywayMigrate
```
4. Generate jOOQ classes based on p.3, execute task:
```
:micronaut-jooq:assemble
```

# micronaut-grpc
1. Describe proto files in micronaut-grpc/src/main/proto folder
2. Execute Gradle task:
```
:micronaut-grpc:assemble
```

# micronaut-app
Build docker image with JRE and push it:
```
cd micronaut-app
./docker-build.sh
```
Run docker image:
```
docker run -d -p 9001:9001 -p 19001:19001 -e DATASOURCE_URL=jdbc:postgresql://[PostgreSQL IP]:5432/micronaut-example alixey/micronaut-example
```

Build docker native image(read first [this](https://guides.micronaut.io/latest/micronaut-creating-first-graal-app-gradle-java.html)):
```
cd micronaut-app
./gradlew dockerBuildNative
```

# asdf
I use [asdf](https://asdf-vm.com/) to manage Java version.

# Deploy to Kubernetes
1. cd kubernetes
2. helm install -f stage.yaml micronaut-app ./micronaut-app
