package fr.shurisko.route.user;

import fr.shurisko.entity.CloudUser;
import fr.shurisko.security.LoginSecurity;
import fr.shurisko.session.CloudSession;
import spark.Request;
import spark.Response;

public class DisplayCommonPage {

    public static String homePage(Request request, Response response) {
        if (!LoginSecurity.kickNonLoggedUser(request, response)) return null;

        CloudUser user = CloudSession.getUser(request);
        return "\t<link rel=\"icon\" type=\"image/png\" href=\"%url/images/icons/favicon.ico\"/>\n" +
                "Salut à tous vous êtes actuellement login! <br> Votre pseudo: " + user.getUsername() +
                "<br> Votre Grade: <span style=\"color: "+ user.getRankManager().getColorHex() + " \">" + user.getRankManager().getRankName() + "</span>" +
                "<form method='post' enctype='multipart/form-data'>" // note the enctype
                + "    <input type='file' name='uploaded_file'>" // make sure to call getPart using the same "name" in the post
                + "    <button>Upload picture</button>"
                + "</form>";
    }

}
