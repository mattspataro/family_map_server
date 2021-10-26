package Handlers.Get;

import Requests.Request;
import Results.ErrorResult;
import Results.Result;
import Services.GetFamilyService;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public class GetFamilyHandler extends GetHandler{
    @Override
    public Request getRequest(HttpExchange exchange) {

        //get and set the authToken
        Headers reqHeaders = exchange.getRequestHeaders();
        if (reqHeaders.containsKey("Authorization")) {
            setAuthTokenID(reqHeaders.getFirst("Authorization"));
        }
        return null;
    }

    @Override
    public Result serveRequest(Request request) {
        GetFamilyService service = new GetFamilyService();
        if(getAuthTokenID() != null){
            return service.getFamily(getAuthTokenID());
        }else{
            return new ErrorResult("No auth token");
        }
    }
}
