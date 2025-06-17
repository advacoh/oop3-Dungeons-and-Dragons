package gameTests;

import game.board.Board;
import game.Position;
import game.units.players.*;
import game.units.enemies.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Warrior, Mage and Rogue – no singleton GameContext.
 * Board(Player, String levelPath) is assumed to load a text-level and link the player.
 */
public class PlayerTest {

    /** Temporary level file: 3×3 board, enemy “s” one tile above the player. */
    private Path levelFile;

    @BeforeEach
    void createLevel() throws Exception {
        String map =
                "...%n" +   // y = 0
                        "s..%n" +   // y = 1  ← enemy
                        "@..%n";    // y = 2  ← player
        levelFile = Files.createTempFile("level", ".txt");
        Files.writeString(levelFile, String.format(map));
    }

    /** Convenience helper – fresh Board for the supplied player. */
    private Board boardWith(Player p) throws Exception {
        return new Board(p, levelFile.toString());
    }

    /* ---------- Warrior ---------- */

    @Test
    void warriorAbility_healsAndDamages_setsCooldown() throws Exception {
        Warrior w = new Warrior('@', "Jon Snow", 300, 30, 4, 3);
        Board board = boardWith(w);

        w.setHealthPool(100);                            // simulate previous damage
        Enemy e = board.getEnemiesInRange(3).get(0);
        int enemyHpBefore = e.getHealthPool();

        w.castAbility(board);

        assertTrue(w.getHealthPool() > 100, "Warrior should heal himself");
        assertTrue(e.getHealthPool() < enemyHpBefore, "Enemy should lose HP");
        assertEquals(w.getAbilityCooldown(), w.getRemainingCooldown());
    }

    @Test
    void warriorTick_decrementsCooldown() throws Exception {
        Warrior w = new Warrior('@', "Jon Snow", 300, 30, 4, 3);
        Board board = boardWith(w);
        w.castAbility(board);

        int before = w.getRemainingCooldown();
        w.tick();
        assertEquals(Math.max(0, before - 1), w.getRemainingCooldown());
    }

    /* ---------- Mage ---------- */

    @Test
    void mageAbility_success_consumesMana_andDamages() throws Exception {
        Mage m = new Mage('@', "Melisandre", 100, 5, 1, 300, 30, 15, 5, 6);
        Board board = boardWith(m);

        int manaBefore = m.getCurrentMana();
        Enemy e = board.getEnemiesInRange(6).get(0);
        int hpBefore = e.getHealthPool();

        m.castAbility(board);

        assertTrue(m.getCurrentMana() < manaBefore, "Mana should drop");
        assertTrue(e.getHealthPool() < hpBefore, "Enemy should be hit");
    }

    @Test
    void mageAbility_fails_ifNoMana() throws Exception {
        Mage m = new Mage('@', "Melisandre", 100, 5, 1, 300, 30, 15, 5, 6);
        m.setCurrentMana(0);
        Board board = boardWith(m);

        Enemy e = board.getEnemiesInRange(6).get(0);
        int hpBefore = e.getHealthPool();

        m.castAbility(board);

        assertEquals(0, m.getCurrentMana());
        assertEquals(hpBefore, e.getHealthPool(), "Enemy HP must remain unchanged");
    }

    @Test
    void mageTick_regeneratesManaByLevel() {
        Mage m = new Mage('@', "Melisandre", 100, 5, 1, 300, 30, 15, 5, 6);
        m.setCurrentMana(0);
        m.tick();
        assertEquals(m.getLevel(), m.getCurrentMana(),
                "Mage gains mana equal to level each tick");
    }

    /* ---------- Rogue ---------- */

    @Test
    void rogueAbility_hitsEnemyAndConsumesEnergy() throws Exception {
        Rogue r = new Rogue('@', "Arya Stark", 150, 40, 2, 20);
        Board board = boardWith(r);

        Enemy e = board.getEnemiesInRange(2).get(0);
        int hpBefore = e.getHealthPool();
        int energyBefore = r.getCurrentEnergy();

        r.castAbility(board);

        assertTrue(r.getCurrentEnergy() < energyBefore, "Energy should be used");
        assertTrue(e.getHealthPool() < hpBefore, "Enemy should be damaged");
    }

    @Test
    void rogueAbility_fails_ifNoEnergy() throws Exception {
        Rogue r = new Rogue('@', "Arya Stark", 150, 40, 2, 20);
        r.setCurrentEnergy(0);
        Board board = boardWith(r);

        Enemy e = board.getEnemiesInRange(2).get(0);
        int hpBefore = e.getHealthPool();

        r.castAbility(board);

        assertEquals(0, r.getCurrentEnergy());
        assertEquals(hpBefore, e.getHealthPool(), "Enemy should not be hit");
    }

    @Test
    void rogueTick_restoresTenEnergy() {
        Rogue r = new Rogue('@', "Arya Stark", 150, 40, 2, 20);
        r.setCurrentEnergy(50);
        r.tick();
        assertEquals(60, r.getCurrentEnergy());
    }

    /* ---------- Shared level-up checks ---------- */

    @Test
    void playersLevelUp_updateStatsCorrectly() {
        Warrior w = new Warrior('@', "Jon Snow", 300, 30, 4, 3);
        w.gainExperience(150);                 // enough for two level-ups
        assertEquals(3, w.getLevel());
        assertTrue(w.getAttack() > 30);

        Mage m = new Mage('@', "Melisandre", 100, 5, 1, 300, 30, 15, 5, 6);
        m.gainExperience(100);
        assertEquals(2, m.getLevel());
        assertTrue(m.getManaPool() > 300);

        Rogue r = new Rogue('@', "Arya Stark", 150, 40, 2, 20);
        r.gainExperience(100);
        assertEquals(2, r.getLevel());
        assertEquals(100, r.getCurrentEnergy());
    }
}