package Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;

// This means that when a user points a web browser at your server
// http://localhost:8080/  OR  http://localhost:8080/index.html

public class FileHandler implements HttpHandler {
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().toUpperCase().equals("GET")) {
            String urlPath = exchange.getRequestURI().toString();

            if(urlPath == null || urlPath.equals("/")){
                urlPath = "/index.html";
            }

            String filePath = "web" + urlPath;
            File file = new File(filePath);
            OutputStream respBody = exchange.getResponseBody();

            if(file.exists()){
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                Files.copy(file.toPath(), respBody);
            }else{
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                file = new File( "web/HTML/404.html");
                Files.copy(file.toPath(), respBody);
            }
            respBody.close();
        }else{
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
        }
        exchange.close();
    }
}
