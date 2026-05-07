# Digital Therapy Assistant - C4 Architecture Diagrams

This directory contains the C4 model architecture diagrams for the Digital Therapy Assistant application, written in PlantUML format. The diagrams follow the [C4 model](https://c4model.com/) methodology, progressively zooming in from system context down to code-level detail.

## Diagrams

### Level 1: System Context (`c4-context.puml`)

The highest-level view showing the Digital Therapy Assistant system in relation to its users (Patient, Therapist, System Administrator) and external systems (LLM Provider, Email Service, Healthcare EHR Systems). This diagram answers the question: "What is the system and who/what interacts with it?"

### Level 2: Container (`c4-container.puml`)

Zooms into the Digital Therapy Assistant system boundary to show the major containers: the Frontend Application (Nginx/React on port 3000), Spring Boot Application (port 8080), MCP Server (stdio), H2 Database, SimpleVector Store, and Knowledge Base. Shows Nginx reverse proxy from frontend to backend, and MCP server integration for AI clients.

### Level 3: Component (`c4-component.puml`)

Zooms into the Spring Boot Application container to show its internal components: REST controllers (Auth, Session, Diary, Progress, Crisis), service layer (AuthService, SessionService, DiaryService, ProgressService, CrisisService, AiService), data repositories, and infrastructure components (RagContextBuilder, LlmClient, CrisisDetector, JWT security). This diagram shows the component-level architecture and their dependencies.

### Level 4: Code (`c4-code.puml`)

Contains three diagrams that zoom into the code level:

- **Class Diagram (AI Service Module):** Shows the class structure of the AI subsystem including the `AiService` interface, `LlmClient`, `RagContextBuilder`, `SimpleVectorStore`, `CrisisDetector`, `EmbeddingService`, and `KnowledgeBaseLoader` with their fields, methods, and relationships.

- **Sequence Diagram (Chat Message Flow):** Traces a user chat message from the CLI through the SessionController, SessionService, AiService (with RAG context building and crisis detection), and back to the user with the AI-generated therapeutic response.

- **Sequence Diagram (Diary Entry with AI Analysis):** Traces the creation of a thought diary entry, including the AI-powered cognitive distortion suggestion step and the subsequent entry persistence.

### Deployment Diagram (`c4-deployment.puml`)

Shows the production deployment architecture: AWS EC2 instance with Docker Compose orchestrating frontend (Nginx Alpine, port 3000) and backend (JRE 22 Alpine, port 8080) containers. Includes persistent storage volumes for H2 database and vector store, security group rules (ports 22, 3000, 8080), Elastic IP, and external service connections to Anthropic Claude API and GitHub infrastructure.

### CI/CD Pipeline Diagram (`c4-pipeline.puml`)

Shows the stage-gate CI/CD/CD pipeline implemented with GitHub Actions:
- **CI (ci.yml):** Build → [Unit Tests (JaCoCo 80%) | Integration Tests | Code Quality (Checkstyle, SpotBugs) | Dependency Check (OWASP) | Security Scan (Gitleaks) | Frontend Lint (ESLint)] — all parallel after build
- **CD Build (cd-build.yml):** Build Docker Images → Push to GHCR → Smoke Test
- **CD Deploy (cd-deploy.yml):** SSH Deploy to EC2 → Docker Compose Up → Post-Deployment Verification

## Rendering the Diagrams

These `.puml` files can be rendered using:

- **PlantUML Online Server:** Paste the contents at [https://www.plantuml.com/plantuml/uml](https://www.plantuml.com/plantuml/uml)
- **VS Code:** Install the "PlantUML" extension by jebbs
- **IntelliJ IDEA:** Install the "PlantUML Integration" plugin
- **Command Line:** `java -jar plantuml.jar c4-context.puml`

Note: The C4 diagrams require internet access on first render to download the C4-PlantUML library includes from GitHub.
