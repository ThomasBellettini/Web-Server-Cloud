package fr.shurisko.http;

import fr.shurisko.entity.CloudUser;
import fr.shurisko.entity.manager.UserManager;
import fr.shurisko.http.api.CloudRoute;
import fr.shurisko.http.api.enumeration.RequestType;
import fr.shurisko.security.LoginSecurity;
import fr.shurisko.session.CloudSession;
import fr.shurisko.utils.Color;
import fr.shurisko.utils.ParserHTML;
import org.apache.commons.io.FileUtils;
import spark.Request;
import spark.Response;
import spark.Session;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;

import static spark.Spark.*;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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
                "<br> Votre Grade: <span style=\"color: "+ user.getRankManager().getColorHex() + " \">" + user.getRankManager().getRankName() + "</span>" +
                "<form method='post' enctype='multipart/form-data'>" // note the enctype
                + "    <input type='file' name='uploaded_file'>" // make sure to call getPart using the same "name" in the post
                + "    <button>Upload picture</button>"
                + "</form>";
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

        File uploadDir = new File("upload");
        uploadDir.mkdir(); // create the upload directory if it doesn't exist

        staticFiles.externalLocation("upload");

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

            post("/home", (req, res) -> {
                CloudUser cloudUser = CloudSession.getUser(req);
                req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
                try (InputStream input = req.raw().getPart("uploaded_file").getInputStream()) { // getPart needs to use same "name" as input field in form
                    File file = new File(uploadDir, cloudUser.ID + getFileName(req.raw().getPart("uploaded_file")));

                    FileUtils.copyInputStreamToFile(input, file);
                    System.out.println(getFileName(req.raw().getPart("uploaded_file")));
                    System.out.println(input);
                    return "<h1>You uploaded this image:<h1><img src='" + file.getAbsolutePath() + "'>";

                }

            });

        }
    }

    private static String getFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                return cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
}
