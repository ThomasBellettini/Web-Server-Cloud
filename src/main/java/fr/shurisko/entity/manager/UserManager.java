package fr.shurisko.entity.manager;

import fr.shurisko.entity.CloudUser;

import java.util.ArrayList;
import java.util.List;

public class UserManager {

    public static UserManager UManager;
    public List<CloudUser> cloudUsers = new ArrayList<>();

    public UserManager() {
        UManager = this;
    }

    public CloudUser getUserByToken(String token) {
        for (CloudUser tmp : cloudUsers) {
            if (tmp.getAuthToken().toString().equals(token)) {
                return tmp;
            }
        }
        return null;
    }

    public void updateUser(CloudUser cloudUser) {
        for (CloudUser tmp : cloudUsers) {
            if (tmp.ID == cloudUser.ID) {
                tmp.setUsername(cloudUser.getUsername());
                tmp.setPassword(cloudUser.getPassword());
                tmp.setAuthToken(cloudUser.getAuthToken());
            }
        }
    }

    public void addUser(CloudUser cloudUser) {
        for (CloudUser tmp : cloudUsers)
            if (tmp.ID == cloudUser.ID)
                return;
        cloudUsers.add(cloudUser);
    }
}
