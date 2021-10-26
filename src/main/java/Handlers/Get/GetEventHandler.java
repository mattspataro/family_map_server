package Handlers.Get;

import Requests.Request;
import Results.ErrorResult;
import Results.Result;
import Services.GetEventService;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public class GetEventHandler extends GetHandler{
    @Override
    public Request getRequest(HttpExchange exchange) {

        //get and set the personID
        String eventID = getParameter(exchange.getRequestURI().getPath());
        this.setEventID(eventID);

        //get and set the authToken
        Headers reqHeaders = exchange.getRequestHeaders();
        if (reqHeaders.containsKey("Authorization")) {
            setAuthTokenID(reqHeaders.getFirst("Authorization"));
        }
        return null;
    }

    @Override
    public Result serveRequest(Request request) {
        GetEventService service = new GetEventService();
        if(getAuthTokenID() != null){
            return service.getEvent(getEventID(),getAuthTokenID());
        }else{
            return new ErrorResult("No auth token");
        }
    }
}
