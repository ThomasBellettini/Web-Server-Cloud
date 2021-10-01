package fr.shurisko.session;

import fr.shurisko.entity.CloudUser;
import fr.shurisko.entity.manager.UserManager;
import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.jetty.server.Authentication;
import spark.Request;
import spark.Response;

import java.util.Locale;
import java.util.UUID;

public class CloudSession {

    private static String SALT_ONE = "EFUHSFLVN-785";
    private static String SALT_TWO = "OEZFJNBVFS-68415325";

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

    public static CloudUser getUserByID(int id) {
        return UserManager.UManager.getUserByID(id);
    }

    public static String hashPass(String pass) {
        return DigestUtils.md5Hex(SALT_ONE + pass + SALT_TWO).toUpperCase(Locale.ROOT);
    }

    public static CloudUser login(Request request) {
        String username = request.params("username");
        String password = request.params("password");

        for (CloudUser cloudUser : UserManager.UManager.cloudUsers) {
            if (cloudUser.getUsername().equalsIgnoreCase(username)) {
                if (hashPass(password).equals(cloudUser.getPassword())) {
                    return cloudUser;
                }
            }
        }
        return null;
    }

    public static CloudUser register(Request request) {
        String username = request.params("username");
        String password = request.params("password");
        String email = request.params("email");

        for (CloudUser cloudUser : UserManager.UManager.cloudUsers) {
            if (cloudUser.getUsername().equalsIgnoreCase(username)) {
                return null;
            }
        }
        CloudUser cloudUser = new CloudUser(username, hashPass(password), email);
        UserManager.UManager.addUser(cloudUser);
        return cloudUser;
    }

}
