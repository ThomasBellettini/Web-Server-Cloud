package fr.shurisko.entity.permission;

import fr.shurisko.utils.Color;

public enum RankManager {

    ADMIN("ADMIN", 0, Color.getRGB(235, 64, 52)),
    DEVELOPER("DEVELOPER", 1, Color.getRGB(3, 94, 252)),
    MANAGER("MANAGER", 2, Color.getRGB(252, 173, 3)),
    FRIEND("FRIEND", 3, Color.getRGB(252, 173, 3)),
    PARTNER("PARTNER", 4, Color.getRGB(194, 3, 252)),
    VIP("VIP", 5, Color.getRGB(248, 252, 3)),
    USER("USER", 6, Color.getRGB(148, 148, 136)),
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
