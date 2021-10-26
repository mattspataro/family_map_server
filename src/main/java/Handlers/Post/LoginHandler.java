package Handlers.Post;

import Requests.LoginRequest;
import Requests.Request;
import Results.Result;
import Services.LoginService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;

public class LoginHandler extends PostHandler {

    @Override
    public Request getRequest(HttpExchange exchange) throws IOException {
        InputStream reqBody = exchange.getRequestBody();
        return convertFromJSON(reqBody, LoginRequest.class);
    }

    @Override
    public Result serveRequest(Request request) {
        LoginService service = new LoginService();
        return service.login((LoginRequest)request);
    }
}
