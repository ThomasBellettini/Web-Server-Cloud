package fr.shurisko.route.admin.dir;

import fr.shurisko.WebCloudServer;
import fr.shurisko.entity.CloudUser;
import fr.shurisko.entity.permission.RankManager;
import fr.shurisko.security.LoginSecurity;
import fr.shurisko.session.CloudSession;
import spark.Request;
import spark.Response;

public class SaveAllDir {

    public static String saveAllDir(Request request, Response response) {
        if (!LoginSecurity.kickNonLoggedUser(request, response)) return null;
        CloudUser cloudUser = CloudSession.getUser(request);
        if (cloudUser.getRankManager() != RankManager.ADMIN) {
            response.redirect("/login");
            return null;
        }
        WebCloudServer.CloudAPI.gsonStorageDir.saveAll();
        response.redirect("/home");
        return null;
    }

}
