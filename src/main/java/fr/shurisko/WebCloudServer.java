package fr.shurisko;

import fr.shurisko.entity.CloudUser;
import fr.shurisko.entity.manager.UserManager;
import fr.shurisko.entity.permission.RankManager;
import fr.shurisko.http.CloudRouteManager;
import fr.shurisko.http.api.CloudRoute;

import java.util.UUID;

public class WebCloudServer {

    public String ressourceURL = "http://icm.shurisko.fr/";

    public static WebCloudServer CloudAPI;
    public CloudRouteManager cloudRouteManager;
    public UserManager userManager;

    public WebCloudServer() {
        CloudAPI = this;
        cloudRouteManager = new CloudRouteManager();
        userManager = new UserManager();

        cloudRouteManager.loadAllRoute();

        userManager.addUser(new CloudUser("Shurisko", "admin", UUID.randomUUID(), RankManager.ADMIN));
    }

    public static void main(String[] args) {
        new WebCloudServer();
    }

}
