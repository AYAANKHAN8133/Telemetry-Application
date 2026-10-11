import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;

public class TelemetryApplication {

    public static void main(String[] args) throws IOException {
        int port = 5000;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        
        server.createContext("/", new HomeHandler());
        server.createContext("/api/telemetry/status", new TelemetryHandler());
        
        server.setExecutor(null);
        server.start();
        System.out.println("✅ Server started successfully on http://localhost:" + port);
    }

    static class HomeHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String htmlResponse = "<html><body style='font-family:sans-serif; text-align:center; padding-top:50px;'>" +
                    "<h1>Enterprise Java Telemetry Node</h1>" +
                    "<p>Status: <span style='color:green; font-weight:bold;'>ONLINE</span></p>" +
                    "<a href='/api/telemetry/status'>View Live JSON Telemetry</a>" +
                    "</body></html>";
            exchange.getResponseHeaders().set("Content-Type", "text/html");
            exchange.sendResponseHeaders(200, htmlResponse.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(htmlResponse.getBytes());
            os.close();
        }
    }

    static class TelemetryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            long uptimeMs = ManagementFactory.getRuntimeMXBean().getUptime();
            long freeMemory = Runtime.getRuntime().freeMemory() / (1024 * 1024);
            long totalMemory = Runtime.getRuntime().totalMemory() / (1024 * 1024);

            String jsonResponse = "{\n" +
                    "  \"status\": \"ONLINE\",\n" +
                    "  \"environment\": \"Standalone Java JVM\",\n" +
                    "  \"uptimeSeconds\": " + (uptimeMs / 1000) + ",\n" +
                    "  \"freeMemoryMB\": " + freeMemory + ",\n" +
                    "  \"totalMemoryMB\": " + totalMemory + ",\n" +
                    "  \"timestamp\": \"" + LocalDateTime.now() + "\"\n" +
                    "}";

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();
        }
    }
}