# SHAFT MCP - Smithery Deployment Guide

## Overview

This guide explains how to deploy SHAFT MCP to Smithery.ai and other hosting platforms. SHAFT MCP supports both STDIO transport (for Claude Desktop) and HTTP/SSE transport (for Smithery and other web-based deployments).

## Smithery Deployment

### Prerequisites

- GitHub account
- Smithery.ai account
- Docker installed (for local testing)

### Deployment Steps

1. **Push Your Code to GitHub**
   
   Ensure your repository contains:
   - `smithery.yaml` - Smithery configuration file
   - `Dockerfile.smithery` - Optimized Dockerfile for Smithery
   - `src/main/resources/application-http.properties` - HTTP transport configuration

2. **Connect Repository to Smithery**
   
   - Visit [Smithery.ai](https://smithery.ai)
   - Click "Deploy New Server"
   - Connect your GitHub repository: `ShaftHQ/SHAFT_MCP`
   - Smithery will automatically detect the `smithery.yaml` file

3. **Configure Deployment**
   
   Smithery will use the configuration in `smithery.yaml`:
   - Runtime: Container
   - Build: `Dockerfile.smithery.build`
   - Start command: HTTP (port 8081)
   - MCP SSE endpoint: `/mcp`
   
4. **Deploy**
   
   - Smithery will build the Docker image from `Dockerfile.smithery.build`
   - The server will start with HTTP/SSE transport enabled
   - The MCP endpoint will be available at `/mcp`

### Local Testing Before Deployment

Test the HTTP transport locally:

```bash
# Build the Docker image
docker build -f Dockerfile.smithery.build -t shaft-mcp-http .

# Run the container
docker run -p 8081:8081 -e PORT=8081 -e SPRING_PROFILES_ACTIVE=http shaft-mcp-http

# Test the endpoint (in another terminal)
curl -N -H "Accept: text/event-stream" http://localhost:8081/mcp
```

### Configuration Options

The `smithery.yaml` supports the following configuration:

```yaml
browserType: "CHROME"  # Options: CHROME, FIREFOX, SAFARI, EDGE
```

## Alternative MCP Server Hosting Platforms

While Smithery provides excellent integration for MCP servers, you have several other hosting options:

### 1. **Cloudflare Workers**
- **Best For**: Edge-hosted, globally distributed MCP servers
- **Pros**: 
  - Free tier (100k requests/day)
  - Minimal cold starts
  - Global edge network
  - One-click deployment
- **Cons**: 
  - Limited to HTTP/SSE transport
  - May not support heavy browser automation
- **Deployment**: Use Cloudflare Workers with custom routing
- **Website**: [https://workers.cloudflare.com/](https://workers.cloudflare.com/)

### 2. **Railway**
- **Best For**: Custom deployments with CI/CD
- **Pros**:
  - Flexible deployment options
  - Good developer experience
  - Support for Docker containers
  - Built-in CI/CD
- **Cons**: 
  - Usage-based pricing can add up
  - Network costs for high traffic
- **Deployment**: Connect GitHub repo and deploy with Docker
- **Website**: [https://railway.app/](https://railway.app/)

### 3. **Glama**
- **Best For**: Beginners and quick prototypes
- **Pros**:
  - Simple UI for deployment
  - Fully hosted solution
  - No infrastructure management
- **Cons**: 
  - Limited free tier
  - Fewer customization options
- **Deployment**: Use their web interface
- **Website**: [https://glama.ai/](https://glama.ai/)

### 4. **Composio**
- **Best For**: Enterprise-grade production deployments
- **Pros**:
  - Managed MCP servers
  - OAuth and token management
  - Production-grade SLAs
  - Built-in integrations
- **Cons**: 
  - Paid plans for managed services
  - More complex setup
- **Deployment**: Use their managed platform
- **Website**: [https://composio.dev/](https://composio.dev/)

### 5. **Pipedream**
- **Best For**: API-heavy MCP servers with workflows
- **Pros**:
  - 2,500+ built-in integrations
  - Workflow automation
  - Strong authentication handling
  - API-first approach
- **Cons**: 
  - Higher costs at scale
  - Learning curve for workflows
- **Deployment**: Use their platform with webhooks
- **Website**: [https://pipedream.com/](https://pipedream.com/)

### 6. **Render**
- **Best For**: Simple container deployments
- **Pros**:
  - Free tier available
  - Easy Docker deployment
  - Auto-scaling
  - Good for small to medium projects
- **Cons**: 
  - Limited free tier resources
  - May have cold starts on free tier
- **Deployment**: Deploy from GitHub with Docker
- **Website**: [https://render.com/](https://render.com/)

### 7. **Fly.io**
- **Best For**: Global distribution and edge deployment
- **Pros**:
  - Global edge network
  - Docker support
  - Good free tier
  - Low latency worldwide
- **Cons**: 
  - Requires Fly CLI for deployment
  - Learning curve for Fly-specific features
- **Deployment**: Use Fly CLI with Docker
- **Website**: [https://fly.io/](https://fly.io/)

### 8. **Google Cloud Run**
- **Best For**: Scalable, serverless container deployments
- **Pros**:
  - Serverless container platform
  - Auto-scaling
  - Pay-per-use pricing
  - Integration with GCP services
- **Cons**: 
  - Requires GCP account and setup
  - Learning curve for GCP
- **Deployment**: Deploy container to Cloud Run
- **Website**: [https://cloud.google.com/run](https://cloud.google.com/run)

### 9. **Azure Container Instances**
- **Best For**: Enterprise Azure deployments
- **Pros**:
  - Fast container startup
  - Azure ecosystem integration
  - Enterprise security features
  - Good for hybrid cloud
- **Cons**: 
  - Higher cost than some alternatives
  - Azure-specific knowledge needed
- **Deployment**: Deploy via Azure Portal or CLI
- **Website**: [https://azure.microsoft.com/en-us/services/container-instances/](https://azure.microsoft.com/en-us/services/container-instances/)

### 10. **AWS App Runner**
- **Best For**: AWS-native deployments
- **Pros**:
  - Simple container deployment on AWS
  - Auto-scaling
  - AWS ecosystem integration
  - Good for existing AWS users
- **Cons**: 
  - AWS account required
  - Can be more expensive
- **Deployment**: Deploy from container registry
- **Website**: [https://aws.amazon.com/apprunner/](https://aws.amazon.com/apprunner/)

## Choosing the Right Platform

| Platform | Best For | Free Tier | Browser Support | Difficulty |
|----------|----------|-----------|-----------------|------------|
| **Smithery** | MCP-specific deployments | Yes | Limited | Easy |
| **Cloudflare Workers** | Edge computing | Yes (100k req) | No | Easy |
| **Railway** | Custom apps with CI/CD | Trial | Yes | Easy |
| **Glama** | Beginners | Limited | No | Very Easy |
| **Composio** | Enterprise | OSS core | Limited | Medium |
| **Pipedream** | API workflows | Yes | Limited | Medium |
| **Render** | Simple deployments | Yes | Yes | Easy |
| **Fly.io** | Global edge | Yes | Yes | Medium |
| **Google Cloud Run** | Scalable serverless | Yes | Yes | Medium |
| **Azure Container Instances** | Enterprise Azure | Trial | Yes | Medium |
| **AWS App Runner** | AWS ecosystem | Limited | Yes | Medium |

### Recommendations

- **For MCP-specific features**: Start with **Smithery**
- **For global edge deployment**: Use **Cloudflare Workers** or **Fly.io**
- **For enterprise production**: Choose **Composio**, **Azure**, or **AWS**
- **For beginners**: Start with **Glama** or **Render**
- **For API-heavy workflows**: Use **Pipedream**
- **For cost-conscious deployments**: Try **Railway** or **Render** free tiers
- **For full browser automation support**: Use **Railway**, **Render**, **Fly.io**, or cloud providers (GCP, Azure, AWS)

## Architecture Differences

### STDIO Transport (Claude Desktop)
- **Use Case**: Local Claude Desktop integration
- **Configuration**: `application.properties` (default)
- **Deployment**: JAR file or Docker with STDIO
- **Profile**: Default (no profile)

### HTTP/SSE Transport (Smithery & Web)
- **Use Case**: Remote web-based access
- **Configuration**: `application-http.properties`
- **Deployment**: Docker container with HTTP endpoint
- **Profile**: `http` (activated via `SPRING_PROFILES_ACTIVE=http`)
- **Endpoint**: `/mcp` (configured via `spring.ai.mcp.server.sse-endpoint=/mcp`)

## Troubleshooting

### Docker Build Fails
- Ensure Java 21 is installed in the base image
- Check that all dependencies are correctly specified in `pom.xml`
- Verify network connectivity for Maven downloads

### HTTP Endpoint Not Responding
- Check that the correct profile is activated: `SPRING_PROFILES_ACTIVE=http`
- Verify the PORT environment variable is set (default: 8081)
- Ensure firewall/security groups allow traffic on the port

### Browser Automation Issues on Smithery
- Some platforms may have limitations on headless browser automation
- Chrome/Chromium must be installed in the Docker image
- Display drivers (Xvfb) may be required for some platforms

## Additional Resources

- [Smithery Documentation](https://smithery.ai/docs)
- [Spring AI MCP Documentation](https://docs.spring.io/spring-ai/reference/api/mcp/)
- [SHAFT Engine Documentation](https://shafthq.github.io/)
- [Model Context Protocol Specification](https://modelcontextprotocol.io/)

## Support

For issues or questions:
- GitHub Issues: [https://github.com/ShaftHQ/SHAFT_MCP/issues](https://github.com/ShaftHQ/SHAFT_MCP/issues)
- SHAFT Documentation: [https://shafthq.github.io/](https://shafthq.github.io/)
