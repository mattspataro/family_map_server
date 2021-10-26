import Handlers.*;
import Handlers.Get.GetAllEventsHandler;
import Handlers.Get.GetEventHandler;
import Handlers.Get.GetFamilyHandler;
import Handlers.Get.GetPersonHandler;
import Handlers.Post.*;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class FamilyMapServer {

    public static void main(String[] args) throws IOException {
        if(args.length > 0){
            int serverPort = Integer.parseInt(args[0]);
            FamilyMapServer server = new FamilyMapServer();
            server.startServer(serverPort);
        } else{
            System.out.println("\nplease enter a port number to start the server...");
        }
    }

    private void startServer(int port) throws IOException {
        InetSocketAddress serverAddress = new InetSocketAddress(port);
        HttpServer server = HttpServer.create(serverAddress, 10);
        registerHandlers(server);
        server.start();
        System.out.println("FamilyMapServer listening on port " + port);
    }
    private void registerHandlers(HttpServer server) {
        server.createContext("/", new FileHandler());
        server.createContext("/user/register", new RegisterHandler());
        server.createContext("/user/login", new LoginHandler());
        server.createContext("/clear", new ClearHandler());
        server.createContext("/fill/", new FillHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/person/", new GetPersonHandler());
        server.createContext("/person", new GetFamilyHandler());
        server.createContext("/event/", new GetEventHandler());
        server.createContext("/event", new GetAllEventsHandler());
    }
}
