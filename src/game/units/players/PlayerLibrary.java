package game.units.players;

import java.util.HashMap;
import java.util.Map;

public class PlayerLibrary {
    private static final Map<String, PlayerFactory> playerMap = new HashMap<>();

    static {
        playerMap.put("Jon Snow", () -> new Warrior('@', "Jon Snow", 300, 30, 4, 3));
        playerMap.put("The Hound", () -> new Warrior('@', "The Hound", 400, 20, 6, 5));

        playerMap.put("Melisandre", () -> new Mage('@', "Melisandre", 100, 5, 1, 300, 30, 15, 5, 6));
        playerMap.put("Thoros of Myr", () -> new Mage('@', "Thoros of Myr", 250, 25, 4, 150, 20, 20, 3, 4));

        playerMap.put("Arya Stark", () -> new Rogue('@', "Arya Stark", 150, 40, 2, 20));
        playerMap.put("Bronn", () -> new Rogue('@', "Bronn", 250, 35, 3, 50));
    }

    public static Player getPlayerByName(String name) {
        PlayerFactory factory = playerMap.get(name);
        if (factory == null) {
            throw new IllegalArgumentException("Unknown player name: " + name);
        }
        return factory.create();
    }

    @FunctionalInterface
    private interface PlayerFactory {
        Player create();
    }
}
