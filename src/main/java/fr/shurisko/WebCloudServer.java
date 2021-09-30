package fr.shurisko;

import fr.shurisko.entity.CloudUser;
import fr.shurisko.entity.manager.UserManager;
import fr.shurisko.entity.permission.RankManager;
import fr.shurisko.http.CloudRouteManager;
import fr.shurisko.http.api.CloudRoute;
import fr.shurisko.storage.GsonStorageCloudAccount;

import java.util.UUID;

public class WebCloudServer {

    public String ressourceURL = "http://icm.shurisko.fr/";

    public static WebCloudServer CloudAPI;
    public CloudRouteManager cloudRouteManager;
    public UserManager userManager;
    public GsonStorageCloudAccount gsonStorageCloudAccount;

    public WebCloudServer() {
        CloudAPI = this;
        userManager = new UserManager();
        gsonStorageCloudAccount = new GsonStorageCloudAccount();
        gsonStorageCloudAccount.loadAll();
        cloudRouteManager = new CloudRouteManager();
        cloudRouteManager.loadAllRoute();
    }

    public static void main(String[] args) {
        new WebCloudServer();
    }

}
