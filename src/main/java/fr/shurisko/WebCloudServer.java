package fr.shurisko;

import fr.shurisko.entity.manager.UserManager;
import fr.shurisko.http.CloudRouteManager;
import fr.shurisko.http.api.CloudRoute;

public class WebCloudServer {

    public static WebCloudServer CloudAPI;
    public CloudRouteManager cloudRouteManager;
    public UserManager userManager;

    public WebCloudServer() {
        CloudAPI = this;
        cloudRouteManager = new CloudRouteManager();
        userManager = new UserManager();

        cloudRouteManager.loadAllRoute();
    }

    public static void main(String[] args) {
        new WebCloudServer();
    }

}
