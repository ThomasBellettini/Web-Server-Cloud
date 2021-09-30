package fr.shurisko.route.admin.account;

import fr.shurisko.WebCloudServer;
import fr.shurisko.entity.CloudUser;
import fr.shurisko.entity.permission.RankManager;
import fr.shurisko.security.LoginSecurity;
import fr.shurisko.session.CloudSession;
import spark.Request;
import spark.Response;

public class ManageAccount {

    public static String saveAllButton(Request request, Response response) {
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

}
