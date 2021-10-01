package fr.shurisko;

import com.google.gson.Gson;
import fr.shurisko.entity.CloudUser;
import fr.shurisko.entity.manager.UserManager;
import fr.shurisko.entity.permission.RankManager;
import fr.shurisko.http.CloudRouteManager;
import fr.shurisko.http.api.CloudRoute;
import fr.shurisko.ressource.manager.CloudFileManager;
import fr.shurisko.storage.GsonStorageCloudAccount;
import fr.shurisko.storage.GsonStorageDir;

import java.io.File;
import java.util.UUID;

public class WebCloudServer {

    public String ressourceURL = "https://cdn.shurisko.fr/";
    public File uploadDir = new File("upload");

    public static WebCloudServer CloudAPI;
    public CloudRouteManager cloudRouteManager;
    public UserManager userManager;
    public CloudFileManager cloudFileManager;
    public GsonStorageCloudAccount gsonStorageCloudAccount;
    public GsonStorageDir gsonStorageDir;

    public WebCloudServer() {
        CloudAPI = this;
        userManager = new UserManager();
        cloudFileManager = new CloudFileManager();
        gsonStorageCloudAccount = new GsonStorageCloudAccount();
        gsonStorageCloudAccount.loadAll();
        gsonStorageDir = new GsonStorageDir();
        gsonStorageDir.loadAll();
        cloudRouteManager = new CloudRouteManager();
        cloudRouteManager.loadAllRoute();
    }

    public static void main(String[] args) {
        new WebCloudServer();
    }

}
