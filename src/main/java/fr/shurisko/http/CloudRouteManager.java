package fr.shurisko.http;

import fr.shurisko.WebCloudServer;
import fr.shurisko.entity.CloudUser;
import fr.shurisko.http.api.CloudByteRoute;
import fr.shurisko.http.api.CloudRoute;
import fr.shurisko.http.api.enumeration.RequestType;
import fr.shurisko.route.admin.account.ManageAccount;
import fr.shurisko.route.connection.LoginForm;
import fr.shurisko.route.connection.RegisterForm;
import fr.shurisko.route.image.DisplayImage;
import fr.shurisko.route.user.DisplayCommonPage;
import fr.shurisko.session.CloudSession;
import fr.shurisko.utils.CloudImageUtils;
import org.apache.commons.io.FileUtils;
import spark.Request;
import spark.Response;

import javax.servlet.MultipartConfigElement;

import static spark.Spark.*;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CloudRouteManager {

    private List<CloudRoute> cloudRoutes = new ArrayList<>();
    private List<CloudByteRoute> cloudBytesRoutes = new ArrayList<>();

    private void makeRoute(String name, RequestType requestType, RouteFunctionManager routeFunctionManager) {
        CloudRoute cloudRoute = new CloudRoute(name, requestType, routeFunctionManager);
        cloudRoutes.add(cloudRoute);
    }

    private void makeRoute(String name, RequestType requestType, RouteByteFunctionManager routeFunctionManager) {
        CloudByteRoute cloudRoute = new CloudByteRoute(name, requestType, routeFunctionManager);
        cloudBytesRoutes.add(cloudRoute);
    }

    public CloudRouteManager() {
        RouteFunctionManager register_function = (request, response) -> RegisterForm.registerRoute(request);
        makeRoute("/register", RequestType.GET, register_function);
        makeRoute("/register/:value", RequestType.GET, register_function);
        RouteFunctionManager login_function = (request, response) -> LoginForm.loginPage(request);
        makeRoute("/login", RequestType.GET, login_function);
        RouteFunctionManager login_protocol = (request, response) -> LoginForm.loginProtocol(request, response);
        makeRoute("/_login/:username/:password", RequestType.GET, login_protocol);
        RouteFunctionManager home_function = (request, response) -> DisplayCommonPage.homePage(request, response);
        makeRoute("/home", RequestType.GET, home_function);
        RouteFunctionManager button_adm_function = (request, response) -> ManageAccount.saveAllButton(request, response);
        makeRoute("/admin/saveProfile", RequestType.GET, button_adm_function);

        RouteFunctionManager register_protocol = (request, response) -> RegisterForm.registerProtocol(request, response);
        makeRoute("/_register/:username/:email/:password", RequestType.GET, register_protocol);

        RouteByteFunctionManager send_image = ((request, response) -> DisplayImage.sendImage(request, response));
        makeRoute("/img/:id", RequestType.GET, send_image);

        RouteFunctionManager redirect_function = (request, response) -> {
            if (CloudSession.isLogged(request) && CloudSession.getUser(request) != null)
                response.redirect("/home");
            else response.redirect("/login");
            return null;
        };
        makeRoute("/", RequestType.GET, redirect_function);
    }

    public void loadAllRoute() {
        WebCloudServer.CloudAPI.uploadDir.mkdir();
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
        }
        for (CloudByteRoute cloudRoute : cloudBytesRoutes) {
            switch (cloudRoute.getRequestType()) {
                case GET:
                    get(cloudRoute.getRouteName(), ((request, response) -> cloudRoute.getFunctionExecutable().routeFunctionByte(request, response)));
                    break;
                case POST:
                    post(cloudRoute.getRouteName(), ((request, response) -> cloudRoute.getFunctionExecutable().routeFunctionByte(request, response)));
                    break;
                case DELETE:
                    delete(cloudRoute.getRouteName(), ((request, response) -> cloudRoute.getFunctionExecutable().routeFunctionByte(request, response)));
                    break;
            }
        }
        post("/home", (req, res) -> {

            CloudUser cloudUser = CloudSession.getUser(req);
            req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
            try (InputStream input = req.raw().getPart("uploaded_file").getInputStream()) { // getPart needs to use same "name" as input field in form
                File file = new File(WebCloudServer.CloudAPI.uploadDir, cloudUser.ID + CloudImageUtils.getFileName(req.raw().getPart("uploaded_file")));

                FileUtils.copyInputStreamToFile(input, file);
                return "<h1>You uploaded this image:<h1><img src='/img/" + file.getName() + "'>";

            }

        });
    }
}
