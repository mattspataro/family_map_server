package Handlers.Post;

import Requests.Request;
import Results.Result;
import Services.FillService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;


public class FillHandler extends PostHandler{
    @Override
    public Request getRequest(HttpExchange exchange) {
        String urlPath = exchange.getRequestURI().getPath();

        //remove the first "/"
        urlPath = urlPath.substring(urlPath.indexOf("/")+1);

        //remove everything up to the second slash
        urlPath = urlPath.substring(urlPath.indexOf("/")+1);

        if(urlPath.contains("/")){
            this.setUsername(urlPath.substring(0,urlPath.indexOf("/")));
            this.setGenerations(Integer.parseInt(urlPath.substring(urlPath.indexOf("/")+1)));
        }else{
           this.setUsername(urlPath);
        }
        return null;
    }

    @Override
    public Result serveRequest(Request request) throws IOException {
        FillService service = new FillService();
        if(this.getGenerations() != 0){
            return service.fill(getUsername(),getGenerations());
        }else{
            return service.fill(getUsername());
        }
    }


}


