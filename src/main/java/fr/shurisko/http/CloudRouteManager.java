package fr.shurisko.http;

import fr.shurisko.http.api.CloudRoute;
import fr.shurisko.http.api.enumeration.RequestType;
import spark.Request;
import spark.Response;

import static spark.Spark.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CloudRouteManager {

    private List<CloudRoute> cloudRoutes = new ArrayList<>();

    public String registerRoute(Request request) {
        request.session().attribute("username", "ADMIN");
        return "<title>CLOUD DEBUG</title>Bonsoir a tous ceci est un systeme d'url dynamique";
    }

    private String loginRoute(Request request) {
        return "<title>Cloud DEBUG</title>Bonsoir ceci est une route autologin " + request.session().attribute("username");
    }

    public CloudRouteManager() {
        RouteFunctionManager f = (request, response) -> registerRoute(request);
        CloudRoute cloudRoute = new CloudRoute("/register", RequestType.GET, f);
        cloudRoutes.add(cloudRoute);
        RouteFunctionManager fs = (request, response) -> loginRoute(request);
        CloudRoute cloudRsoutes = new CloudRoute("/login", RequestType.GET, fs);
        cloudRoutes.add(cloudRsoutes);
    }

    public void loadAllRoute() {
        port(8000);
        for (CloudRoute cloudRoute : cloudRoutes) {
            switch (cloudRoute.getRequestType()) {
                case GET:
                    get(cloudRoute.getRouteName(), ((request, response) -> cloudRoute.getFunctionExecutable().routeFunction(request, response)));
                    break;
                case POST:
                    post(cloudRoute.getRouteName(), ((request, response) -> cloudRoute.getFunctionExecutable().routeFunction(request, response)));
                    break;
                case DELETE:
                    delete(cloudRoute.getRouteName(), ((request, response) -> cloudRoute.getFunctionExecutable().routeFunction(request, response)));
                    break;
            }
        }
    }
}
