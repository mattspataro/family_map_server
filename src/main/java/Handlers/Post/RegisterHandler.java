package Handlers.Post;

import Requests.RegisterRequest;
import Requests.Request;
import Results.Result;
import Services.RegisterService;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;

public class RegisterHandler extends PostHandler {

    @Override
    public Request getRequest(HttpExchange exchange) throws IOException {
        InputStream reqBody = exchange.getRequestBody();
        return convertFromJSON(reqBody, RegisterRequest.class);
    }

    @Override
    public Result serveRequest(Request request) {
        RegisterService service = new RegisterService();
        return service.register((RegisterRequest)request);
    }

}
