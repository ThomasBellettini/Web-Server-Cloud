package fr.shurisko.security;

import fr.shurisko.entity.CloudUser;
import fr.shurisko.entity.permission.RankManager;
import spark.Request;
import spark.Response;

public class checkBanStatement {

    public static boolean checkUserBan(Request request, Response response, CloudUser cloudUser) {
        if (cloudUser.getRankManager() == RankManager.BANNED) {
            response.redirect("/banned");
            return true;
        }
        return false;
    }

}
