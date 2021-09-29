package fr.shurisko.entity;

import fr.shurisko.entity.manager.UserManager;
import fr.shurisko.entity.permission.RankManager;

import java.util.UUID;

public class CloudUser {

    public static int ID;
    private String username;
    private String password;
    private RankManager rankManager;
    private transient UUID authToken;

    public CloudUser(String username, String password, UUID authToken, RankManager rankManager) {
        this.username = username;
        this.password = password;
        this.authToken = authToken;
        this.rankManager = rankManager;
        ID = UserManager.UManager.cloudUsers.size() + 1;
    }

    public CloudUser(String username, String password) {
        this(username, password, null, RankManager.USER);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UUID getAuthToken() {
        return authToken;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthToken(UUID authToken) {
        this.authToken = authToken;
    }

    public RankManager getRankManager() {
        return rankManager;
    }

    public void setRankManager(RankManager rankManager) {
        this.rankManager = rankManager;
    }
}
