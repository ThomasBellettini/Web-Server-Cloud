package fr.shurisko.ressource;

import fr.shurisko.entity.CloudUser;
import fr.shurisko.ressource.permission.CloudRessourcePermission;

import java.util.HashMap;
import java.util.Map;

public class CloudRessource {

    public int ID;
    private int ownerID;

    private boolean isDirectory;

    private String resourceName;
    private String resourceDescription;

    private Map<CloudUser, CloudRessourcePermission> individualPermission;
    private CloudRessourcePermission publicPermission;

    public CloudRessource(int ID, int ownerID, String resourceName, String resourceDescription, Map<CloudUser, CloudRessourcePermission> individualPermission, CloudRessourcePermission publicPermission, boolean isDirectory) {
        this.ID = ID;
        this.ownerID = ownerID;
        this.resourceName = resourceName;
        this.resourceDescription = resourceDescription;
        this.individualPermission = individualPermission;
        this.publicPermission = publicPermission;
        this.isDirectory = isDirectory;
    }

    public CloudRessource(int ID, int ownerID, String resourceName, String resourceDescription, boolean isDirectory) {
        this(ID, ownerID, resourceName, resourceDescription, new HashMap<>(), new CloudRessourcePermission(), isDirectory);
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceDescription() {
        return resourceDescription;
    }

    public void setResourceDescription(String resourceDescription) {
        this.resourceDescription = resourceDescription;
    }

    public Map<CloudUser, CloudRessourcePermission> getIndividualPermission() {
        return individualPermission;
    }

    public void setIndividualPermission(Map<CloudUser, CloudRessourcePermission> individualPermission) {
        this.individualPermission = individualPermission;
    }

    public CloudRessourcePermission getPublicPermission() {
        return publicPermission;
    }

    public void setPublicPermission(CloudRessourcePermission publicPermission) {
        this.publicPermission = publicPermission;
    }
}
