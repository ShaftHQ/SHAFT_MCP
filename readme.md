# To install
- close any open claude desktop app instance.
- run the following command in your terminal:
```bash
mvn clean package -DskipTests
```

# To test
- close any open claude desktop app instance.
- run the following command in your terminal:
```bash
mvn clean test
```

# To configure with claude desktop app
- close any open claude desktop app instance.
- edit this file `C:\Users\${USERNAME}\AppData\Roaming\Claude\claude_desktop_config.json`
- include this code block:
```json
{
  "mcpServers": {
    "jetbrains": {
      "command": "npx",
      "args": [
        "-y",
        "@jetbrains/mcp-proxy"
      ]
    },
    "github": {
      "command": "npx",
      "args": [
        "-y",
        "@modelcontextprotocol/server-github"
      ],
      "env": {
        "GITHUB_PERSONAL_ACCESS_TOKEN": "${GITHUB_PERSONAL_ACCESS_TOKEN}"
      }
    },
    "shaft-mcp": {
      "command": "java",
      "args": [
        "-jar",
        "C:\\Users\\Mohab\\IdeaProjects\\SHAFT_MCP\\target\\SHAFT_MCP-9.3.20250824.jar"
      ]
    }
  }
}
```
- don't forget to replace `SHAFT_MCP-9.3.20250823` with the latest version.
- don't forget to replace `${GITHUB_PERSONAL_ACCESS_TOKEN}` with your actual GitHub token.

# To run
- open claude desktop app.
- navigate to `file > settings > developer > shaft-mcp`.
- confirm that the local mcp server status is `running`.
- input this prompt into the claude chat window:
```
use shaft-mcp to launch chrome, navigate to duckduckgo, search for selenium webdriver, open the first search result, and check that the url is correct.
```