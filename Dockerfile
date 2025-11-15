FROM eclipse-temurin:21-jre

# MCP Registry validation label
LABEL io.modelcontextprotocol.server.name="io.github.shafthq/shaft-mcp"

# Download the released JAR from Maven Central
ADD https://repo1.maven.org/maven2/io/github/shafthq/SHAFT_MCP/9.4.20251108/SHAFT_MCP-9.4.20251108.jar /app/SHAFT_MCP.jar

WORKDIR /app

# Run the main class from the JAR
CMD ["java", "-jar", "SHAFT_MCP.jar"]