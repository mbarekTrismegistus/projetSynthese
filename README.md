# eBank Microservices — projet de synthèse

Java 21 / Spring Boot 3.5.10 microservices, Spring Cloud, Spring AI 1.1.2, MCP Streamable HTTP and an Angular 21 client.

## Architecture

```text
Angular :4200 ──> Gateway :9999 ──> Customer Service :8056
                         │          EBank Service :8057 ──OpenFeign──> Customer Service
                         └────────> eBank Chatbot :8058
                                      ├── MCP Streamable HTTP ──> Customer Service
                                      └── MCP Streamable HTTP ──> EBank Service
                                                  │
                                           OpenAI Chat Model

Discovery / Eureka :8761 registers the services. Config Server :8888 is available for
centralized configuration.
Telegram and Discord connect directly to the chatbot service, outside the gateway.
```

The business services use in-memory H2 databases and seed Mohammed, Iman, and Yassine on first startup. A bank account stores a `customerId`; EBank Service fetches customer details synchronously with OpenFeign and returns a fallback customer if Customer Service is unavailable. The same business operations are exposed as MCP tools for the Spring AI agent.

## Requirements

- Java 21
- Maven 3.9+
- Node.js 22 and npm
- An OpenAI API key for chatbot requests
- Optional Telegram and Discord bot tokens for those clients

Set credentials through environment variables; never commit API keys or bot tokens. Set `OPENAI_API_KEY` in the shell that starts the chatbot. Set `TELEGRAM_BOT_TOKEN` and `DISCORD_BOT_TOKEN` if you want to enable those integrations.

## Start the system

From the repository root, build all Java modules with `mvn -f backend/pom.xml package`. Start each module in its own terminal, in this order:

1. `cd backend/discovery-service && mvn spring-boot:run`
2. `cd backend/customer-service && mvn spring-boot:run`
3. `cd backend/ebank-service && mvn spring-boot:run`
4. `cd backend/gateway && mvn spring-boot:run`
5. Set `OPENAI_API_KEY`, then `cd backend/ebank-chatbot && mvn spring-boot:run`

The Config Server can be started with `cd backend/config-server && mvn spring-boot:run`. It is optional for the default setup because each service currently reads its own `application.yml`; the services are not configured to fetch settings from the Config Server. The server uses the native profile, and `CONFIG_REPO` sets the directory from which it serves configuration files.

Start Angular in another terminal: `cd frontend && npm install && npm start`, then open `http://localhost:4200`.

## REST and Swagger checks

The same paths work directly against the services or through the gateway at port 9999.

| Operation | Request |
| --- | --- |
| List customers | `GET http://localhost:9999/customers` |
| Find customer | `GET http://localhost:9999/customers/1` |
| Create customer | `POST http://localhost:9999/customers` with `{"name":"Sara","email":"sara@example.com"}` |
| List accounts | `GET http://localhost:9999/accounts` |
| Accounts for customer | `GET http://localhost:9999/accounts/customer/1` |
| Create account | `POST http://localhost:9999/accounts` with `{"balance":4000,"type":"current account","customerId":1}` |
| Ask chatbot | `GET http://localhost:9999/chat?query=Liste%20des%20clients` or POST plain text to `/chat` |

Swagger UI is available at `http://localhost:8056/swagger-ui.html` and `http://localhost:8057/swagger-ui.html`. Eureka's dashboard is at `http://localhost:8761`.

The MCP endpoints use `/mcp` on ports 8056 and 8057. In Postman, create an MCP request with Streamable HTTP to `http://localhost:8056/mcp` or `http://localhost:8057/mcp` and list/call the advertised tools. The chatbot loads the same tools from these two endpoints at startup.

## Telegram and Discord

Create a Telegram bot with BotFather and set `TELEGRAM_BOT_TOKEN` before starting the chatbot. It polls Telegram for text messages and sends each question to the same `EBankAgent` used by the web controller. The Telegram library's current Spring Boot long-polling integration follows its [official Spring Boot example](https://rubenlagus.github.io/TelegramBotsDocumentation/lesson-9.html).

Create a Discord application and bot, invite it to a server with application-command permission, and set `DISCORD_BOT_TOKEN` before starting the chatbot. The bot registers `/ask query:<question>` and sends the question to the same agent. Its listener uses JDA's slash-command interaction model.

Tokens are optional; the chatbot starts without either bot. Discord slash commands may take a short time to appear after their initial registration.

## Data storage

Customer Service and EBank Service use in-memory H2 databases. Their data resets when the corresponding service restarts. The chatbot requires a valid OpenAI API key for model requests; CRUD and MCP operations do not.
