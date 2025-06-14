package game.units.enemies;

import game.Position;
import java.util.HashMap;
import java.util.Map;

public class EnemyLibrary {
    private static final Map<Character, EnemyFactory> enemyMap = new HashMap<>();

    static {
        // Monsters
        enemyMap.put('s', pos -> new Monster("Lannister Soldier", 's', 5, 8, 3, 3, 25, pos));
        enemyMap.put('k', pos -> new Monster("Lannister Knight", 'k', 200, 14, 8, 4, 50, pos));
        enemyMap.put('q', pos -> new Monster("Queen’s Guard", 'q', 400, 20, 15, 5, 100, pos));
        enemyMap.put('z', pos -> new Monster("Wright", 'z', 600, 30, 15, 3, 100, pos));
        enemyMap.put('b', pos -> new Monster("Bear-Wright", 'b', 1000, 75, 30, 4, 250, pos));
        enemyMap.put('g', pos -> new Monster("Giant-Wright", 'g', 1500, 100, 40, 5, 500,pos));
        enemyMap.put('w', pos -> new Monster("White Walker", 'w', 2000, 150, 50, 6, 1000, pos));
        enemyMap.put('M', pos -> new Monster("The Mountain", 'M', 1000, 60, 25, 6, 500, pos));
        enemyMap.put('C', pos -> new Monster("Queen Cersei", 'C', 100, 10, 10, 1, 1000, pos));
        enemyMap.put('K', pos -> new Monster("Night’s King", 'K', 5000, 300, 150, 8, 5000, pos));

        // Traps
        enemyMap.put('B', pos -> new Trap("Bonus Trap", 'B', 1, 1, 1, 250, 1, 5, pos));
        enemyMap.put('Q', pos -> new Trap("Queen’s Trap", 'Q', 250, 50, 10, 100, 3, 7, pos));
        enemyMap.put('D', pos -> new Trap("Death Trap", 'D', 500, 100, 20, 250, 1, 10, pos));
    }

    public static Enemy getEnemyByTile(char tile, Position pos) {
        EnemyFactory factory = enemyMap.get(tile);
        if (factory == null) {
            throw new IllegalArgumentException("Unknown enemy tile: " + tile);
        }
        return factory.create(pos);
    }

    @FunctionalInterface
    private interface EnemyFactory {
        Enemy create(Position pos);
    }
}
