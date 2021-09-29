package fr.shurisko.security;

import fr.shurisko.session.CloudSession;
import spark.Request;
import spark.Response;

public class LoginSecurity {

    public static boolean kickNonLoggedUser(Request request, Response response) {
        String token = request.session().attribute("cloudToken");
        if (!CloudSession.isLogged(token)) {
            response.redirect("/login");
            return false;
        }
        return true;
    }

}
