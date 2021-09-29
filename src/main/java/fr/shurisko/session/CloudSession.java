package fr.shurisko.session;

import fr.shurisko.entity.CloudUser;
import fr.shurisko.entity.manager.UserManager;
import spark.Request;
import spark.Response;

import java.util.UUID;

public class CloudSession {

    public static boolean isLogged(Request request) {
        if (request.session().attribute("cloudToken") != null) {
            CloudUser cloudUser = UserManager.UManager.getUserByToken(request.session().attribute("cloudToken"));
            if (cloudUser == null) return false;
            else return true;
        }
        return false;
    }

    public static boolean isLogged(String request) {
        CloudUser cloudUser = UserManager.UManager.getUserByToken(request);
        if (cloudUser == null) return false;
        else return true;

    }

    public static void makeAuthToken(CloudUser cloudUser) {
        cloudUser.setAuthToken(UUID.randomUUID());
        UserManager.UManager.updateUser(cloudUser);
    }
    
    public static CloudUser getUser(Request request){
        if (request.session().attribute("cloudToken") != null) {
            CloudUser cloudUser = UserManager.UManager.getUserByToken(request.session().attribute("cloudToken"));
            if (cloudUser == null) return null;
            else return cloudUser;
        }
        return null;
    }

    public static CloudUser login(Request request) {
        String username = request.params("username");
        String password = request.params("password");

        for (CloudUser cloudUser : UserManager.UManager.cloudUsers) {
            if (cloudUser.getUsername().equalsIgnoreCase(username)) {
                if (password.equals(cloudUser.getPassword())) {
                    return cloudUser;
                }
            }
        }
        return null;
    }

}
