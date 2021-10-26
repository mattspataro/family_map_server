package Handlers.Post;

import Requests.Request;
import Results.Result;
import Services.ClearService;
import com.sun.net.httpserver.HttpExchange;

public class ClearHandler extends PostHandler {
    @Override
    public Request getRequest(HttpExchange exchange) {
        return null;
    }

    @Override
    public Result serveRequest(Request request) {
        ClearService service = new ClearService();
        return service.clear();
    }
}
