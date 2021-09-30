package fr.shurisko.http;

import fr.shurisko.WebCloudServer;
import fr.shurisko.entity.CloudUser;
import fr.shurisko.entity.manager.UserManager;
import fr.shurisko.entity.permission.RankManager;
import fr.shurisko.http.api.CloudByteRoute;
import fr.shurisko.http.api.CloudRoute;
import fr.shurisko.http.api.enumeration.RequestType;
import fr.shurisko.security.LoginSecurity;
import fr.shurisko.session.CloudSession;
import fr.shurisko.utils.ParserHTML;
import org.apache.commons.io.FileUtils;
import spark.Request;
import spark.Response;

import javax.imageio.ImageIO;
import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;

import static spark.Spark.*;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CloudRouteManager {

    private File uploadDir = new File("upload");

    private List<CloudRoute> cloudRoutes = new ArrayList<>();
    private List<CloudByteRoute> cloudBytesRoutes = new ArrayList<>();

    public String loginPage(Request request) {
        return ParserHTML.parseHTML("login");
    }

    private byte[] noPermission() {
        byte[] rawImage = new byte[0];
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            BufferedImage bi;
            File img = new File(uploadDir, "no_permission.jpg");
            if (!img.getName().contains(".")) return null;
            String[] ext = {"jpeg", "png", "jpg", "gif", "svg"};
            String extension = "";
            for (String tmp : ext) {
                if (img.getName().endsWith(tmp)) {
                    extension = tmp;
                    break;
                }
            }
            bi = ImageIO.read(img);
            ImageIO.write( bi, extension, baos );

            baos.flush();
            rawImage = baos.toByteArray();
        } catch (IOException ignored) { }
        return rawImage;
    }

    public byte[] sendImage(Request request, Response response) {
        if (!LoginSecurity.kickNonLoggedUser(request, response, false)) {
            response.type("image/jpeg");
            return noPermission();
        } else {
            byte[] rawImage = new byte[0];
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                BufferedImage bi;
                File img = new File(uploadDir, request.params("id"));
                if (!img.getName().contains(".")) return null;
                String[] ext = {"jpeg", "png", "jpg", "gif", "svg"};
                String extension = "";
                for (String tmp : ext) {
                    if (img.getName().endsWith(tmp)) {
                        extension = tmp;
                        break;
                    }
                }
                bi = ImageIO.read(img);
                ImageIO.write(bi, extension, baos);

                baos.flush();
                rawImage = baos.toByteArray();
            } catch (IOException ignored) {
            }
            response.type("image/jpeg");
            return rawImage;
        }
    }

    private String saveAllButton(Request request, Response response) {
        if (!LoginSecurity.kickNonLoggedUser(request, response)) return null;
        CloudUser cloudUser = CloudSession.getUser(request);
        if (cloudUser.getRankManager() != RankManager.ADMIN) {
            response.redirect("/login");
            return null;
        }
        WebCloudServer.CloudAPI.gsonStorageCloudAccount.saveAll();
        response.redirect("/home");
        return null;
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

    public String registerProtocol(Request request, Response response) {
        CloudUser user = CloudSession.register(request);
        if (user == null) response.redirect("/register");
        else {
            if (user.getAuthToken() == null) CloudSession.makeAuthToken(user);
            request.session().attribute("cloudToken", user.getAuthToken().toString());
            response.redirect("/home");
        }
        return null;
    }

    public String registerRoute(Request request) {
        String value = "";
        if (request.params("value") != null) {
            value = request.params("value");
        }
        return ParserHTML.parseHTML("register").replace("%value", value);
    }

    private void makeRoute(String name, RequestType requestType, RouteFunctionManager routeFunctionManager) {
        CloudRoute cloudRoute = new CloudRoute(name, requestType, routeFunctionManager);
        cloudRoutes.add(cloudRoute);
    }

    private void makeRoute(String name, RequestType requestType, RouteByteFunctionManager routeFunctionManager) {
        CloudByteRoute cloudRoute = new CloudByteRoute(name, requestType, routeFunctionManager);
        cloudBytesRoutes.add(cloudRoute);
    }

    public CloudRouteManager() {
        RouteFunctionManager register_function = (request, response) -> registerRoute(request);
        makeRoute("/register", RequestType.GET, register_function);
        makeRoute("/register/:value", RequestType.GET, register_function);
        RouteFunctionManager login_function = (request, response) -> loginPage(request);
        makeRoute("/login", RequestType.GET, login_function);
        RouteFunctionManager login_protocol = (request, response) -> loginProtocol(request, response);
        makeRoute("/_login/:username/:password", RequestType.GET, login_protocol);
        RouteFunctionManager home_function = (request, response) -> homePage(request, response);
        makeRoute("/home", RequestType.GET, home_function);
        RouteFunctionManager button_adm_function = (request, response) -> saveAllButton(request, response);
        makeRoute("/admin/saveProfile", RequestType.GET, button_adm_function);

        RouteFunctionManager register_protocol = (request, response) -> registerProtocol(request, response);
        makeRoute("/_register/:username/:email/:password", RequestType.GET, register_protocol);

        RouteByteFunctionManager send_image = ((request, response) -> sendImage(request, response));
        makeRoute("/img/:id", RequestType.GET, send_image);
    }

    public void loadAllRoute() {
        uploadDir.mkdir();
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
                File file = new File(uploadDir, cloudUser.ID + getFileName(req.raw().getPart("uploaded_file")));

                FileUtils.copyInputStreamToFile(input, file);
                return "<h1>You uploaded this image:<h1><img src='/img/" + file.getName() + "'>";

            }

        });
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
