package fr.shurisko.entity.permission;

public enum PermissionManager {

    READ(0),
    WRITE(1),
    MAKE_LINK(2),
    MANAGE_PERMISSION(3),
    CAN_INTERACT(4);

    private int permissionValue;

    PermissionManager(int permissionValue) {
        this.permissionValue = permissionValue;
    }

    public static PermissionManager getManagePermission(int permissionValue) {
        for (PermissionManager manager : PermissionManager.values())
            if (manager.permissionValue == permissionValue)
                return manager;
        return null;
    }
}
