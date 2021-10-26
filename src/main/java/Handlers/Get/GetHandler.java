package Handlers.Get;

import Requests.Request;
import Results.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;

public abstract class GetHandler implements HttpHandler{

    private String authTokenID;
    private String personID;
    private String eventID;

    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("GET")) {

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

    public String getParameter(String urlPath){
        //remove the first "/"
        urlPath = urlPath.substring(urlPath.indexOf("/")+1);
        //remove everything up to the second slash
        urlPath = urlPath.substring(urlPath.indexOf("/")+1);
        return urlPath;
    }

    public String getAuthTokenID() {
        return authTokenID;
    }

    public void setAuthTokenID(String authTokenID) {
        this.authTokenID = authTokenID;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

}

