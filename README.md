<div align="center">

# 📡 Enterprise Java Telemetry Node

### Lightweight Java HTTP server → JVM runtime metrics → JSON telemetry

A minimal, dependency-free Java telemetry service for exposing live JVM health and runtime information over HTTP.

![Java](https://img.shields.io/badge/Java-17%2B-orange?logo=openjdk&logoColor=white)
![HTTP](https://img.shields.io/badge/Protocol-HTTP-blue)
![Telemetry](https://img.shields.io/badge/Telemetry-Live-success)
![Dependencies](https://img.shields.io/badge/Dependencies-Zero-brightgreen)
![Status](https://img.shields.io/badge/Status-ONLINE-success)

</div>

---

## 📡 What Does This Project Do?

This project implements a lightweight *Java-based telemetry node* that starts an HTTP server on port 5000 and exposes live runtime information from the Java Virtual Machine.

The application provides two endpoints:

- / → Simple server health/status page
- /api/telemetry/status → Live JVM telemetry in JSON format

The telemetry endpoint collects runtime information such as:

- 🟢 Server status
- ☕ Java/JVM environment
- ⏱️ JVM uptime
- 💾 Free memory
- 🧠 Total allocated memory
- 🕐 Current timestamp

---

## 🗺️ Architecture at a Glance

mermaid
flowchart LR
    A[/"🌐 Browser / Client"/] -->|"HTTP Request"| B[/"☕ Java HTTP Server<br/>Port 5000"/]

    B -->|"Route /"| C["🟢 Health Status"]

    B -->|"GET /api/telemetry/status"| D[/"📡 Telemetry Handler"/]

    D -->|"Read Runtime Data"| E[("🧠 Java JVM")]

    E -->|"Uptime<br/>Memory<br/>Environment"| D

    D -->|"JSON Response"| A


---

## 🎬 3D Telemetry Flow

The following animation visualizes how a request travels through the application, how the JVM telemetry is collected, and how the resulting JSON response is returned to the client.

<p align="center">
  <img src="assets/telemetry_3d_architecture.gif"
       alt="3D animation of the Java telemetry architecture"
       width="900">
</p>

---

## 🔄 Request Flow

| # | Stage | Component | What happens |
|---|---|---|---|
| 1️⃣ | *Request* | Browser / Client | Sends an HTTP request |
| 2️⃣ | *Server* | Java HTTP Server | Receives the request on port 5000 |
| 3️⃣ | *Routing* | HTTP Context | Determines which handler should process the request |
| 4️⃣ | *Telemetry* | TelemetryHandler | Reads live JVM runtime information |
| 5️⃣ | *Processing* | Java Runtime | Calculates uptime and memory values |
| 6️⃣ | *Response* | JSON | Packages telemetry into a JSON response |
| 7️⃣ | *Client* | Browser / Monitor | Receives and displays the telemetry |

---

## ⚙️ How the Server Works

The application creates a Java HTTP server:

java
int port = 5000;

HttpServer server =
    HttpServer.create(new InetSocketAddress(port), 0);


Two HTTP routes are registered:

java
server.createContext("/", new HomeHandler());

server.createContext(
    "/api/telemetry/status",
    new TelemetryHandler()
);


The server is then started:

java
server.start();


Once running, the application becomes available at:

text
http://localhost:5000


---

## 🟢 Endpoint 1 — Health Check

### GET /

The root endpoint provides a simple human-readable health page.

text
Enterprise Java Telemetry Node

Status: ONLINE

View Live JSON Telemetry


It also provides a link to:

text
/api/telemetry/status


This makes it easy to verify that the server itself is running correctly.

---

## 📊 Endpoint 2 — Live Telemetry

### GET /api/telemetry/status

The telemetry endpoint reads information directly from the running JVM.

### Runtime metrics collected

| Metric | Source | Description |
|---|---|---|
| status | Application | Current application state |
| environment | Application | JVM execution environment |
| uptimeSeconds | ManagementFactory | How long the JVM has been running |
| freeMemoryMB | Runtime | Currently available JVM memory |
| totalMemoryMB | Runtime | Total memory allocated to the JVM |
| timestamp | LocalDateTime | Time at which telemetry was generated |

The core telemetry collection is performed using:

java
long uptimeMs =
    ManagementFactory
        .getRuntimeMXBean()
        .getUptime();

long freeMemory =
    Runtime.getRuntime()
        .freeMemory() / (1024 * 1024);

long totalMemory =
    Runtime.getRuntime()
        .totalMemory() / (1024 * 1024);


---

## 📦 Example JSON Response

json
{
  "status": "ONLINE",
  "environment": "Standalone Java JVM",
  "uptimeSeconds": 125,
  "freeMemoryMB": 241,
  "totalMemoryMB": 512,
  "timestamp": "2026-10-11T23:40:00"
}


This response can be consumed by:

- 🌐 Web applications
- 📊 Monitoring dashboards
- 🤖 Automated monitoring systems
- 🔌 Other backend services
- 🧪 Testing and diagnostic tools

---

## 🧠 Internal Architecture

text
                         ┌──────────────────────┐
                         │    🌐 Client         │
                         │  Browser / Monitor   │
                         └──────────┬───────────┘
                                    │
                              HTTP Request
                                    │
                                    ▼
                         ┌──────────────────────┐
                         │ ☕ Java HTTP Server  │
                         │      Port 5000       │
                         └──────────┬───────────┘
                                    │
                       ┌────────────┴────────────┐
                       │                         │
                       ▼                         ▼
                ┌──────────────┐         ┌─────────────────┐
                │ HomeHandler  │         │ TelemetryHandler│
                │      /       │         │ /api/telemetry/ │
                └──────────────┘         └────────┬────────┘
                                                   │
                                           Read JVM Metrics
                                                   │
                                                   ▼
                                          ┌─────────────────┐
                                          │   Java JVM      │
                                          │                 │
                                          │ • Uptime        │
                                          │ • Free Memory   │
                                          │ • Total Memory  │
                                          │ • Timestamp     │
                                          └────────┬────────┘
                                                   │
                                              JSON Output
                                                   │
                                                   ▼
                                          ┌─────────────────┐
                                          │ 🌐 HTTP Client  │
                                          └─────────────────┘


---

## 🛠️ Technology Stack

| Technology | Purpose |
|---|---|
| ☕ *Java* | Core application |
| 🌐 *Java HttpServer* | HTTP server implementation |
| 🧠 *JVM Management API* | Runtime telemetry |
| 💾 *Runtime API* | Memory statistics |
| 🕐 *LocalDateTime* | Timestamp generation |
| 📦 *JSON* | Telemetry response format |

### External dependencies

*None.*

The application uses Java's built-in APIs:

java
com.sun.net.httpserver.HttpServer
com.sun.net.httpserver.HttpHandler
java.lang.management.ManagementFactory
java.time.LocalDateTime
java.lang.Runtime


---

## ⚡ Quick Start

### 1️⃣ Compile

bash
javac TelemetryApplication.java


### 2️⃣ Run

bash
java TelemetryApplication


You should see:

text
✅ Server started successfully on http://localhost:5000


### 3️⃣ Open the health page

text
http://localhost:5000


### 4️⃣ View live telemetry

text
http://localhost:5000/api/telemetry/status


---

## 📁 Project Structure

text
📦 Enterprise-Java-Telemetry-Node
│
├── ☕ TelemetryApplication.java
├── 📖 README.md
│
└── 🎬 assets/
    └── telemetry_3d_architecture.gif


---

## 🎯 Why This Project?

The project demonstrates how a Java application can expose its own runtime health information through a lightweight HTTP interface.

Instead of relying on a complex monitoring framework, the application demonstrates the underlying mechanism:

text
Application
     ↓
HTTP Endpoint
     ↓
JVM Runtime
     ↓
Telemetry Metrics
     ↓
JSON
     ↓
Monitoring Client


This makes the project useful as a simple foundation for understanding:

- Application health monitoring
- JVM observability
- HTTP-based telemetry
- Runtime metrics
- Backend API design
- Monitoring architecture

---

## 🧩 Design Highlights

| | Design Decision | Why it matters |
|---|---|---|
| ☕ | *Pure Java* | Demonstrates the underlying server implementation |
| 🪶 | *Lightweight* | No external framework required |
| 🌐 | *HTTP-based* | Can be consumed by browsers and monitoring systems |
| 📡 | *Live telemetry* | Metrics are generated when the endpoint is requested |
| 📦 | *JSON output* | Easy for other systems to consume |
| 🧠 | *JVM-aware* | Reads actual runtime information |
| 🔌 | *Extensible* | Additional telemetry metrics can be added easily |

---

## 🚀 Future Improvements

Possible extensions include:

- 📈 CPU utilization monitoring
- 🧵 Active thread count
- 🧠 Heap vs non-heap memory
- 🗑️ Garbage collection statistics
- 💽 Disk utilization
- 🌐 Network statistics
- 🔐 Authentication for telemetry endpoints
- 📊 Web-based monitoring dashboard
- 🚨 Health thresholds and alerts
- 📡 Prometheus-compatible metrics
- ☁️ Cloud deployment and centralized monitoring

---

<div align="center">

### 📡 From JVM Runtime → HTTP → JSON → Observability

*Built with ☕ Java and a focus on lightweight application telemetry.*

</div>
