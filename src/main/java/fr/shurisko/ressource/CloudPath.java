package fr.shurisko.ressource;

import fr.shurisko.entity.CloudUser;
import fr.shurisko.ressource.permission.CloudRessourcePermission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CloudPath {

    public int ID;
    private int parentID;
    private int ownerID;

    private String name;
    private String description;

    private Map<CloudUser, CloudRessourcePermission> individualPermission;
    private CloudRessourcePermission publiPermission;

    private List<CloudRessource> resourceList;

    public CloudPath(int ID, int parentID, int ownerID, String name, String description, Map<CloudUser, CloudRessourcePermission> individualPermission, CloudRessourcePermission publiPermission, List<CloudRessource> resourceList) {
        this.ID = ID;
        this.parentID = parentID;
        this.ownerID = ownerID;
        this.name = name;
        this.description = description;
        this.individualPermission = individualPermission;
        this.publiPermission = publiPermission;
        this.resourceList = resourceList;
    }

    public CloudPath(int ID, int parentID, int ownerID, String name, String description) {
        this(ID, parentID, ownerID, name, description, new HashMap<>(), new CloudRessourcePermission(), new ArrayList<>());
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPubliPermission(CloudRessourcePermission publiPermission) {
        this.publiPermission = publiPermission;
    }

    public int getParentID() {
        return parentID;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Map<CloudUser, CloudRessourcePermission> getIndividualPermission() {
        return individualPermission;
    }

    public CloudRessourcePermission getPubliPermission() {
        return publiPermission;
    }

    public List<CloudRessource> getResourceList() {
        return resourceList;
    }
}
