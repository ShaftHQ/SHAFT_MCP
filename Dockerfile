FROM eclipse-temurin:21-jre

# Download the released JAR from Maven Central
ADD https://repo1.maven.org/maven2/io/github/shafthq/SHAFT_MCP/9.3.20250823/SHAFT_MCP-9.3.20250823.jar /app/SHAFT_MCP.jar

WORKDIR /app

# Run the main class from the JAR
CMD ["java", "-jar", "SHAFT_MCP.jar"]