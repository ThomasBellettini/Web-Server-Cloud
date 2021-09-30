package fr.shurisko.route.connection;

import fr.shurisko.entity.CloudUser;
import fr.shurisko.session.CloudSession;
import fr.shurisko.utils.ParserHTML;
import spark.Request;
import spark.Response;

import java.util.UUID;

public class LoginForm {

    public static String loginPage(Request request) {
        return ParserHTML.parseHTML("login");
    }

    public static String loginProtocol(Request request, Response response) {
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
}
