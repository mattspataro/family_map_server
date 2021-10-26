package Handlers.Post;

import Requests.LoadRequest;
import Requests.Request;
import Results.Result;
import Services.LoadService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;

public class LoadHandler extends PostHandler{
    @Override
    public Request getRequest(HttpExchange exchange) throws IOException {
        InputStream reqBody = exchange.getRequestBody();
        return convertFromJSON(reqBody, LoadRequest.class);
    }

    @Override
    public Result serveRequest(Request request) {
        LoadService service = new LoadService();
        return service.load((LoadRequest)request);
    }
}
