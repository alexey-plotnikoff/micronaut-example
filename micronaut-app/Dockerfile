FROM eclipse-temurin:17-jre-ubi9-minimal

WORKDIR /app
ADD build/libs/app.jar /app/app.jar
EXPOSE 9001 19001
CMD ["java", "-jar", "/app/app.jar"]
