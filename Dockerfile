FROM ghcr.io/graalvm/graalvm-community:17 AS builder

# Install dependencies and Gradle
RUN microdnf install -y findutils unzip
RUN curl -L https://services.gradle.org/distributions/gradle-8.6-bin.zip -o gradle.zip && \
    unzip gradle.zip && \
    mv gradle-8.6 /opt/gradle && \
    rm gradle.zip
ENV PATH="/opt/gradle/bin:${PATH}"

WORKDIR /app
COPY . .

# Build the application
# We skip tests to speed up the build in this environment
RUN gradle build -x test --no-daemon

# Run stage
FROM ghcr.io/graalvm/graalvm-community:17

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose the port
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]