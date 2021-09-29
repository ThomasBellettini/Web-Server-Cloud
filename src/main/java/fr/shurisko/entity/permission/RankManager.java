package fr.shurisko.entity.permission;

import fr.shurisko.utils.Color;

public enum RankManager {

    ADMIN("ADMIN", 0, Color.getRGB(0, 0, 0)),
    DEVELOPER("DEVELOPER", 1, Color.getRGB(0, 0, 0)),
    MANAGER("MANAGER", 2, Color.getRGB(0, 0, 0)),
    FRIEND("FRIEND", 3, Color.getRGB(0, 0, 0)),
    PARTNER("PARTNER", 4, Color.getRGB(0, 0, 0)),
    VIP("VIP", 5, Color.getRGB(0, 0, 0)),
    USER("USER", 6, Color.getRGB(0, 0, 0)),
    BANNED("BANNED", 7, Color.getRGB(0, 0, 0));

    private String rankName;
    private int rankClass;
    private String colorHex;

    RankManager(String rankName, int rankClass, String colorHex) {
        this.rankName = rankName;
        this.rankClass = rankClass;
        this.colorHex = colorHex;
    }

    public static RankManager getRankPerName(String rankName) {
        for (RankManager rankManager : RankManager.values())
            if (rankManager.getRankName().equals(rankName))
                return rankManager;
        return null;
    }

    public String getRankName() {
        return rankName;
    }

    public int getRankClass() {
        return rankClass;
    }

    public String getColorHex() {
        return colorHex;
    }
}
