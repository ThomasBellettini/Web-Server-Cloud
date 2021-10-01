package fr.shurisko.ressource.manager;

import fr.shurisko.entity.CloudUser;
import fr.shurisko.ressource.CloudPath;
import fr.shurisko.ressource.CloudRessource;

import java.util.*;

public class CloudFileManager {

    public static CloudFileManager PManager;
    public Map<CloudUser, List<CloudPath>> userCloudPath = new HashMap<>();

    public CloudFileManager() { PManager = this; }

    public void addOrUpdatePath(CloudUser userId, CloudPath path) {
        if (userCloudPath.containsKey(userId)) {
            List<CloudPath> l = userCloudPath.get(userId);
            l.removeIf(tmp -> tmp.ID == path.ID);
            l.add(path);
        } else {
            List<CloudPath> cl = new ArrayList<>();
            cl.add(path);
            userCloudPath.put(userId, cl);
        }
    }

}
