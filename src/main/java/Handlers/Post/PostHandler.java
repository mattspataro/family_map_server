package Handlers.Post;

import Requests.Request;
import Results.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;

public abstract class PostHandler implements HttpHandler {
    private String username;
    private int generations;

    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {

                //GET REQUEST
                Request request = getRequest(exchange);
                //SERVE REQUEST
                Result result = serveRequest(request);
                //SEND RESPONSE HEADERS
                sendResponseHeaders(result, exchange);
                //SEND RESPONSE
                sendResponse(result,exchange);

            }else{
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
        }catch(IOException | RuntimeException ex){
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            ex.printStackTrace();
        } finally{
            exchange.close();
        }
    }
    public abstract Request getRequest(HttpExchange exchange) throws IOException;
    public abstract Result serveRequest(Request request) throws IOException;

    public Request convertFromJSON(InputStream inputStream, Type classToConvertTo) throws IOException {
        Gson gson = new Gson();
        try (inputStream; BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            return gson.fromJson(bufferedReader, classToConvertTo);
        }
    }
    private String convertToJSON(Result result){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(result);
    }

    private void sendResponseHeaders(Result result, HttpExchange exchange) throws IOException {
        if(result.isSuccess()){
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        }else{
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
        }
    }

    private void sendResponse(Result result,HttpExchange exchange) throws IOException {
        String jsonString = convertToJSON(result);
        OutputStream respBody = exchange.getResponseBody();
        writeResponse(jsonString, respBody);
        respBody.close();
    }
    private void writeResponse(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(sw);
        bw.write(str);
        bw.flush();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getGenerations() {
        return generations;
    }

    public void setGenerations(int generations) {
        this.generations = generations;
    }
}
