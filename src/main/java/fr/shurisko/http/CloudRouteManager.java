package fr.shurisko.http;

import fr.shurisko.entity.CloudUser;
import fr.shurisko.http.api.CloudRoute;
import fr.shurisko.http.api.enumeration.RequestType;
import fr.shurisko.security.LoginSecurity;
import fr.shurisko.session.CloudSession;
import fr.shurisko.utils.Color;
import fr.shurisko.utils.ParserHTML;
import spark.Request;
import spark.Response;

import static spark.Spark.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CloudRouteManager {

    private List<CloudRoute> cloudRoutes = new ArrayList<>();

    public String loginPage(Request request) {
        return ParserHTML.parseHTML("login");
    }

    public String homePage(Request request, Response response) {
        if (!LoginSecurity.kickNonLoggedUser(request, response)) return null;
        CloudUser user = CloudSession.getUser(request);
        return "Salut à tous vous êtes actuellement login! <br> Votre pseudo: " + user.getUsername() +
                "<br> Votre Grade: <span style=\"color: "+ user.getRankManager().getColorHex() + " \">" + user.getRankManager().getRankName() + "</span>";
    }

    public String loginProtocol(Request request, Response response) {
        CloudUser user = CloudSession.login(request);
        UUID tmp = UUID.randomUUID();
        if (user == null) response.redirect("/login");
        else {
            if (user.getAuthToken() == null) CloudSession.makeAuthToken(user);
            request.session().attribute("cloudToken", user.getAuthToken().toString());
            response.redirect("/home");
        }
        return null;
    }

    public String registerRoute(Request request) {
        String value = "";
        if (CloudSession.isLogged(request)) {
            value = CloudSession.getUser(request).getUsername();
        }
        return ParserHTML.parseHTML("register").replace("%value", value);
    }

    private void makeRoute(String name, RequestType requestType, RouteFunctionManager routeFunctionManager) {
        CloudRoute cloudRoute = new CloudRoute(name, requestType, routeFunctionManager);
        cloudRoutes.add(cloudRoute);
    }

    public CloudRouteManager() {
        RouteFunctionManager register_function = (request, response) -> registerRoute(request);
        makeRoute("/register", RequestType.GET, register_function);
        RouteFunctionManager login_function = (request, response) -> loginPage(request);
        makeRoute("/login", RequestType.GET, login_function);
        RouteFunctionManager login_protocol = (request, response) -> loginProtocol(request, response);
        makeRoute("/_login/:username/:password", RequestType.GET, login_protocol);
        RouteFunctionManager home_function = (request, response) -> homePage(request, response);
        makeRoute("/home", RequestType.GET, home_function);
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
