# SHAFT MCP - GitHub Copilot Instructions

## Project Overview

SHAFT MCP is a **Model Context Protocol (MCP) server** that enables Claude Desktop to perform web automation tasks using the SHAFT Engine (a Selenium-based test automation framework). The project is built with:

- **Language**: Java 21 (OpenJDK 21.0.2 LTS or newer)
- **Framework**: Spring Boot 3.5.7 with Spring AI 1.0.3
- **Build Tool**: Maven 3.x
- **Testing Framework**: JUnit Jupiter
- **Web Automation**: SHAFT Engine 9.4.20251108 (Selenium-based)
- **Distribution**: JAR file, Docker image, Maven Central

## Technology Stack

- Spring Boot for application framework
- Spring AI for MCP server implementation
- SHAFT Engine for browser automation (Selenium WebDriver)
- Allure for test reporting
- SLF4J for logging

## Build & Test Commands

### Build the Project
```bash
mvn clean package -DskipTests -Dgpg.skip
```
This creates the JAR file in `target/SHAFT_MCP-9.4.20251108.jar`

### Run Tests
```bash
mvn clean test
```

### Full Build with Tests
```bash
mvn clean install
```

### Run the Application Locally (for testing)
```bash
java -jar target/SHAFT_MCP-9.4.20251108.jar
```

## Project Structure

```
SHAFT_MCP/
├── src/
│   ├── main/
│   │   ├── java/io/github/shafthq/SHAFT_MCP/
│   │   │   ├── ShaftMcpApplication.java    # Main Spring Boot application
│   │   │   ├── EngineService.java          # Driver lifecycle management
│   │   │   ├── BrowserService.java         # Browser control tools
│   │   │   ├── ElementService.java         # Element interaction tools
│   │   │   ├── BrowserType.java            # Browser type enum
│   │   │   └── locatorStrategy.java        # Element locator strategies
│   │   └── resources/
│   │       └── application.properties      # Spring configuration
│   └── test/
│       └── java/io/github/shafthq/SHAFT_MCP/
│           ├── ShaftMcpApplicationTests.java
│           └── EngineServiceTest.java
├── pom.xml                                 # Maven configuration
├── Dockerfile                              # Docker image configuration
└── readme.md                               # User documentation
```

## Key Components

### 1. MCP Tools (Spring AI @Tool annotations)
- All tools are annotated with `@Tool` from `org.springframework.ai.tool.annotation.Tool`
- Tools are organized across three service classes:
  - **EngineService**: Driver initialization and lifecycle (`driver_initialize`, `driver_quit`, `generate_test_report`)
  - **BrowserService**: Browser operations (`browser_navigate`, `browser_refresh`, `browser_get_current_url`, etc.)
  - **ElementService**: Element interactions (`element_click`, `element_type`, `element_get_text`, etc.)

### 2. SHAFT Engine Integration
- Uses `SHAFT.GUI.WebDriver` for browser automation
- Supports Chrome, Firefox, Safari, and Edge browsers
- Provides AI-enhanced element detection via SHAFT Engine
- Generates Allure test reports

### 3. Spring AI MCP Server
- Configured via `application.properties` with STDIO transport
- Banner and console logging must be disabled for STDIO to work
- Server name: `shaft-mcp`

## Coding Standards & Best Practices

### Code Style
- Follow existing Java conventions in the codebase
- Use descriptive variable names and method names
- Include JavaDoc comments for public methods
- Use SLF4J logger for logging, not System.out.println

### Tool Development
When adding new MCP tools:
1. Add methods to the appropriate service class (EngineService, BrowserService, or ElementService)
2. Annotate with `@Tool(name = "tool_name", description = "clear description")`
3. Include comprehensive JavaDoc explaining parameters and behavior
4. Handle exceptions properly with try-catch blocks
5. Log significant actions and errors using the logger
6. Test the tool with unit tests in the corresponding test class

### Error Handling
- Always wrap SHAFT operations in try-catch blocks
- Log errors with context using `logger.error()`
- Re-throw exceptions to allow proper error reporting to MCP clients
- Include meaningful error messages

### Testing
- Write unit tests for new service methods
- Use JUnit Jupiter (JUnit 5) for test cases
- Test classes should mirror the structure: `ServiceName` → `ServiceNameTest`
- Run tests before committing: `mvn clean test`

## Important Configuration Files

### pom.xml
- Contains project version (must match in Dockerfile, application.properties, readme.md)
- Defines build commands in properties:
  - `commandToPackage`: `mvn clean package "-DskipTests" "-Dgpg.skip"`
  - `commandToTest`: `mvn clean test`
- Maven plugins for compilation, testing, and packaging

### application.properties
- MCP server configuration
- **Critical**: `spring.main.banner-mode=off` and `logging.pattern.console=` must remain empty for STDIO transport

### Dockerfile
- Multi-stage build for creating Docker images
- Published to GitHub Container Registry: `ghcr.io/shafthq/shaft-mcp`

## Version Management

When updating the version:
1. Update `<version>` in `pom.xml`
2. Update `spring.ai.mcp.server.version` in `application.properties`
3. Update version references in `Dockerfile`
4. Update version in `readme.md` installation instructions

Current version: `9.4.20251108`

## Dependencies

Key dependencies managed in pom.xml:
- `spring-boot-starter` (3.5.7)
- `spring-ai-mcp-spring-boot-starter` (1.0.3)
- `SHAFT_ENGINE` (9.4.20251108)
- `aspectjweaver` (1.9.25)
- `junit-jupiter-engine` (6.0.1)

## Files to Ignore

The following should never be committed (see `.gitignore`):
- `target/` - Maven build artifacts
- `allure-results/`, `allure-report/` - Test reports
- IDE-specific files (`.idea/`, `.vscode/`, `.settings/`, etc.)
- `HELP.md` - Auto-generated Spring Boot help

## Documentation

### User-Facing Documentation
- `readme.md` - Complete setup and usage guide for end users
- Includes installation instructions for both JAR and Docker
- Documents all 25+ available MCP tools
- Provides usage examples and troubleshooting

### Code Documentation
- Use JavaDoc for all public methods
- Include parameter descriptions and return value documentation
- Document exceptions that may be thrown

## Testing the MCP Server

To test the MCP server locally:

1. Build the project: `mvn clean package -DskipTests -Dgpg.skip`
2. Configure Claude Desktop to use the JAR file
3. Test with prompts like:
   ```
   Use shaft-mcp to launch Chrome, navigate to google.com, and get the page title.
   ```

## Common Tasks

### Adding a New Browser Tool
1. Add method to `BrowserService.java`
2. Annotate with `@Tool`
3. Use `getDriver()` to access the SHAFT WebDriver
4. Call appropriate `driver.browser()` method
5. Add error handling and logging
6. Write unit tests in `BrowserServiceTest.java`

### Adding a New Element Tool
1. Add method to `ElementService.java`
2. Annotate with `@Tool`
3. Accept locator parameters (strategy, value)
4. Use `getDriver().element()` for operations
5. Add error handling and logging
6. Write unit tests

### Updating Dependencies
1. Check for updates to Spring Boot, Spring AI, or SHAFT Engine
2. Update versions in `pom.xml`
3. Run `mvn clean test` to verify compatibility
4. Update readme.md if needed

## Review Criteria

Pull requests should:
- [ ] Include clear, focused changes
- [ ] Have passing tests (`mvn clean test`)
- [ ] Build successfully (`mvn clean package -DskipTests -Dgpg.skip`)
- [ ] Include JavaDoc for new public methods
- [ ] Follow existing code style and patterns
- [ ] Update documentation if user-facing features changed
- [ ] Not break existing MCP tools
- [ ] Handle errors gracefully with proper logging

## Known Constraints

- This is an MCP server using STDIO transport - console output must be carefully managed
- Browser automation requires the target browser to be installed on the host system
- SHAFT Engine generates Allure reports which should be gitignored
- Docker images must be built with headless browser support

## Additional Resources

- SHAFT Engine Documentation: https://shafthq.github.io/
- Model Context Protocol Spec: https://modelcontextprotocol.io/
- Spring AI MCP: https://docs.spring.io/spring-ai/reference/api/mcp/index.html
- Maven Central: https://central.sonatype.com/artifact/io.github.shafthq/SHAFT_MCP
