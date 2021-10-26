package Handlers.Get;

import Requests.Request;
import Results.ErrorResult;
import Results.Result;
import Services.GetPersonService;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public class GetPersonHandler extends GetHandler {

    @Override
    public Request getRequest(HttpExchange exchange) {

        //get and set the personID
        String personID = getParameter(exchange.getRequestURI().getPath());
        this.setPersonID(personID);

        //get and set the authToken
        Headers reqHeaders = exchange.getRequestHeaders();
        if (reqHeaders.containsKey("Authorization")) {
            setAuthTokenID(reqHeaders.getFirst("Authorization"));
        }
        return null;
    }

    @Override
    public Result serveRequest(Request request) {
        GetPersonService service = new GetPersonService();
        if(getAuthTokenID() != null){
            return service.getPerson(getPersonID(),getAuthTokenID());
        }else{
            return new ErrorResult("No auth token");
        }
    }
}
