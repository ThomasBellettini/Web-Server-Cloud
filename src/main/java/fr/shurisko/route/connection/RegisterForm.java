package fr.shurisko.route.connection;

import fr.shurisko.entity.CloudUser;
import fr.shurisko.session.CloudSession;
import fr.shurisko.utils.ParserHTML;
import spark.Request;
import spark.Response;

public class RegisterForm {

    public static String registerRoute(Request request) {
        String value = "";
        if (request.params("value") != null) {
            value = request.params("value");
        }
        return ParserHTML.parseHTML("register").replace("%value", value);
    }

    public static String registerProtocol(Request request, Response response) {
        CloudUser user = CloudSession.register(request);
        if (user == null) response.redirect("/register");
        else {
            if (user.getAuthToken() == null) CloudSession.makeAuthToken(user);
            request.session().attribute("cloudToken", user.getAuthToken().toString());
            response.redirect("/home");
        }
        return null;
    }

}
