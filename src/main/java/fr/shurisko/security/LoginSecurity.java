package fr.shurisko.security;

import fr.shurisko.session.CloudSession;
import org.apache.commons.codec.digest.DigestUtils;
import spark.Request;
import spark.Response;

import java.util.Locale;

public class LoginSecurity {

    public static boolean kickNonLoggedUser(Request request, Response response) {
        String token = request.session().attribute("cloudToken");
        if (token == null || !CloudSession.isLogged(token)) {
            response.redirect("/login");
            return false;
        }
        if (CloudSession.getUser(request) == null) {
            response.redirect("/login");
            return false;
        }
        return true;
    }

    public static boolean kickNonLoggedUser(Request request, Response response, boolean redirect) {
        String token = request.session().attribute("cloudToken");
        if (token == null || !CloudSession.isLogged(token) && redirect) {
            response.redirect("/login");
            return false;
        } else if (!CloudSession.isLogged(token)) {
            return false;
        }
        if (CloudSession.getUser(request) == null) {
            return false;
        }
        return true;
    }

}
