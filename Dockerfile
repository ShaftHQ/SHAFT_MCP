FROM eclipse-temurin:21-jre

# MCP Registry validation label
LABEL io.modelcontextprotocol.server.name="io.github.ShaftHQ/shaft-mcp"

# Download the released JAR from Maven Central
ADD https://repo1.maven.org/maven2/io/github/shafthq/SHAFT_MCP/9.4.20251116/SHAFT_MCP-9.4.20251116.jar /app/SHAFT_MCP.jar

WORKDIR /app

# Run the main class from the JAR
CMD ["java", "-jar", "SHAFT_MCP.jar"]