# Digital Therapy Assistant

[![CI](https://github.com/jf700/Assignment3_SE320/actions/workflows/ci.yml/badge.svg)](https://github.com/jf700/Assignment3_SE320/actions/workflows/ci.yml)
[![CD Build](https://github.com/jf700/Assignment3_SE320/actions/workflows/cd-build.yml/badge.svg)](https://github.com/jf700/Assignment3_SE320/actions/workflows/cd-build.yml)
[![CD Deploy](https://github.com/jf700/Assignment3_SE320/actions/workflows/cd-deploy.yml/badge.svg)](https://github.com/jf700/Assignment3_SE320/actions/workflows/cd-deploy.yml)

AI-guided CBT platform for workplace burnout recovery, built with Spring Boot and Next.js.

## Prerequisites

- Java 21+
- Maven 3.9+
- Node.js 22+ with pnpm
- Docker Desktop (for containerized deployment)

## Quick Start

```bash
mvn clean install
mvn exec:java "-Dexec.mainClass=com.digitaltherapy.DigitalTherapyAssistantApplication"
```

Access points:
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- H2 Console: http://localhost:8080/h2-console/login.jsp (JDBC URL: `jdbc:h2:file:./data/digitaltherapy_db`, user: `sa`, no password)
- Frontend: http://localhost:3000/

## Frontend (Next.js)

The frontend is a Next.js single-page application in the `mindbridge/` directory that replaces the original CLI interface.

### Setup

```bash
cd mindbridge
pnpm install
pnpm dev
```

The dev server runs on port 3000 and proxies API requests to the Spring Boot backend on port 8080 via Next.js rewrites configured in `next.config.mjs`.

### Features

- **Authentication** — Register, login, logout with JWT token handling and automatic refresh
- **CBT Sessions** — Browse session library, start sessions with real-time AI chat, view history
- **Thought Diary** — Create entries with situation/thought/emotions, AI-suggested distortions, reframing prompts
- **Progress Dashboard** — Weekly summary, monthly trends, burnout metrics, achievements
- **Crisis Support** — Coping strategies, emergency resources, safety plan view and edit

### Technical Details

- Uses Fetch API with centralized `api-client.ts` service layer
- JWT tokens stored in localStorage with automatic 401 refresh flow
- Responsive design with Tailwind CSS (mobile and web layouts)
- Protected routes require authentication

### CLI Deprecation

The original CLI from Assignment 2 remains in the codebase but is **disabled by default** via `app.cli.enabled=false` in `application.properties`. To re-enable, set `app.cli.enabled=true`.

## MCP Server

The application includes an MCP (Model Context Protocol) server that exposes therapeutic tools, resources, and prompts for AI clients like Claude Desktop.

### Setup

Launch in stdio mode:

```bash
java -jar target/digitaltherapy-0.0.1-SNAPSHOT.jar --spring.profiles.active=mcp
```

### Claude Desktop Configuration

Add to your Claude Desktop config (see `docs/mcp/claude-desktop-config.json`):

```json
{
  "mcpServers": {
    "digital-therapy": {
      "command": "java",
      "args": ["-jar", "target/digitaltherapy-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=mcp"],
      "env": {
        "ANTHROPIC_API_KEY": "<your-key>",
        "JWT_SECRET": "<your-secret>"
      }
    }
  }
}
```

### MCP Tools (12)

| Tool | Description |
|------|-------------|
| `start_session` | Start a new CBT session for a user |
| `chat_in_session` | Send a message in an active session |
| `end_session` | End a session with summary |
| `get_session_library` | List available CBT session modules |
| `get_session_history` | View user's past sessions |
| `create_diary_entry` | Create a thought diary entry |
| `analyze_thought` | Analyze a thought for cognitive distortions |
| `suggest_reframing` | Generate reframing prompts |
| `detect_crisis` | Analyze text for crisis indicators |
| `get_weekly_progress` | Get weekly progress summary |
| `get_insights` | Get AI-generated diary insights |
| `get_coping_strategies` | Retrieve coping strategies |

### MCP Resources (7)

| URI | Description |
|-----|-------------|
| `therapy://sessions/{sessionId}` | Session details |
| `therapy://diary/{userId}` | User's diary entries |
| `therapy://diary/entry/{entryId}` | Single diary entry detail |
| `therapy://progress/{userId}` | User's progress overview |
| `therapy://distortions` | Cognitive distortion definitions |
| `therapy://crisis/resources` | Emergency resources and contacts |
| `therapy://safety-plan/{userId}` | User's safety plan |

### MCP Prompts (3)

| Prompt | Description |
|--------|-------------|
| `thought_analysis` | Structured analysis of automatic thoughts for cognitive distortions |
| `session_summary` | Generate a therapeutic session summary |
| `weekly_check_in` | Guided weekly check-in template with mood and progress questions |

## Docker Deployment

### Build and Run

```bash
docker compose up -d --build
```

- Frontend: http://localhost:3000
- Backend/Swagger: http://localhost:8080/swagger-ui/index.html
- H2 Console: http://localhost:8080/h2-console/login.jsp

### Architecture

- **Backend**: Multi-stage Dockerfile (Maven build → JRE 22 runtime), non-root user, health check
- **Frontend**: Multi-stage Dockerfile (Node.js build → Nginx Alpine on port 3000), proxies `/api/*` to backend
- **Docker Compose**: Orchestrates both services with shared network, health check dependencies, and persistent volumes for H2 database and vector store

### AWS EC2 Deployment

See [docs/deployment/DEPLOYMENT.md](docs/deployment/DEPLOYMENT.md) for step-by-step EC2 deployment instructions.

## CI/CD/CD Pipeline

The project uses three GitHub Actions workflows following the stage-gate pattern:

### CI (`ci.yml`)

Triggered on push/PR to `main` and `develop`. Runs build then five parallel quality gates:

```
Build → [Unit Tests (JaCoCo 80%) | Integration Tests | Code Quality (Checkstyle + SpotBugs) | Dependency Check (OWASP) | Security Scan (Gitleaks)] + Frontend Lint
```

### CD Build (`cd-build.yml`)

Triggered on push to `main`. Builds Docker images, pushes to GitHub Container Registry with commit SHA tags, then runs smoke tests.

### CD Deploy (`cd-deploy.yml`)

Triggered after CD Build or via manual dispatch. Deploys to EC2 via SSH, runs `docker compose up`, and verifies health endpoints.

## Architecture Diagrams

See [docs/architecture/README.md](docs/architecture/README.md) for full C4 model documentation:

- **System Context** — External actors and system boundaries
- **Container** — Frontend (Nginx), Backend (Spring Boot), MCP Server, H2 DB, Vector Store
- **Deployment** — AWS EC2, Docker Compose, security groups, volumes
- **CI/CD Pipeline** — Stage-gate pattern across all three workflows
- **Component** — Controllers, services, repositories, AI integration
- **Code** — Class and sequence diagrams
