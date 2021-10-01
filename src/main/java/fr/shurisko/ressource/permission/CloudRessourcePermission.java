package fr.shurisko.ressource.permission;

public class CloudRessourcePermission {

    private boolean canRead;
    private boolean canEdit;
    private boolean canDownload;
    private boolean canEditPermission;
    private boolean canMakeLink;
    private String password;

    public CloudRessourcePermission() {
        canRead = false;
        canEdit = false;
        canDownload = false;
        canEditPermission = false;
        canMakeLink = false;
        password = null;
    }

    public boolean isCanRead() {
        return canRead;
    }

    public void setCanRead(boolean canRead) {
        this.canRead = canRead;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public boolean isCanDownload() {
        return canDownload;
    }

    public void setCanDownload(boolean canDownload) {
        this.canDownload = canDownload;
    }

    public boolean isCanEditPermission() {
        return canEditPermission;
    }

    public void setCanEditPermission(boolean canEditPermission) {
        this.canEditPermission = canEditPermission;
    }

    public boolean isCanMakeLink() {
        return canMakeLink;
    }

    public void setCanMakeLink(boolean canMakeLink) {
        this.canMakeLink = canMakeLink;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
