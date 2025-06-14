//package gameTests;
//
//import game.tiles.Empty;
//import game.tiles.Wall;
//import game.units.*;
//import game.board.*;
//import game.enemies.*;
//import game.*;
//import game.units.enemies.Enemy;
//import game.units.enemies.Trap;
//import game.units.players.Mage;
//import game.units.players.Rogue;
//import game.units.players.Warrior;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//
//public class PlayerTest {
//
//    private Warrior warrior;
//    private Mage mage;
//    private Rogue rogue;
//    private Trap dummyTrap;
//
//    @BeforeEach
//    void setUp() {
//        warrior = new Warrior('@', "Jon Snow", 300, 30, 4, 3);
//        mage = new Mage('@', "Melisandre", 100, 5, 1, 300, 30, 15, 5, 6);
//        rogue = new Rogue('@', "Arya Stark", 150, 40, 2, 20);
//        dummyTrap = new Trap("Queen's trap", 'T', 100, 10, 5, 1, 3, 2, new Position(1,1));
//    }
//
//    @Test
//    void tick() {
//        int initialEnergy = rogue.getCurrentEnergy();
//        int initialMana = mage.getCurrentMana();
//
//        rogue.tick();
//        mage.tick();
//        warrior.tick();
//
//        assertEquals(Math.min(100, initialEnergy + 10), rogue.getCurrentEnergy());
//        assertEquals(Math.min(mage.getManaPool(), initialMana + mage.getLevel()), mage.getCurrentMana());
//        assertEquals(Math.max(0, warrior.getRemainingCooldown() - 1), warrior.getRemainingCooldown());
//    }
//
//    @Test
//    void gainExperience() {
//        warrior.gainExperience(300); // Level up multiple times
//        assertEquals(4, warrior.getLevel());
//        assertEquals(0, warrior.getExperience());
//        assertEquals(315, warrior.getHealth().getPool());
//        assertEquals(36, warrior.getAttack());
//        assertEquals(7, warrior.getDefense());
//    }
//
//    @Test
//    void levelUp() {
//        warrior.levelUp();
//        assertEquals(2, warrior.getLevel());
//        assertEquals(0, warrior.getExperience());
//        assertEquals(305, warrior.getHealth().getPool());
//        assertEquals(32, warrior.getAttack());
//        assertEquals(5, warrior.getDefense());
//    }
//
//    @Test
//    void engage() {
//        Enemy dummyEnemy = new Monster('E', new Position(0, 1), "Dummy", 100, 10, 2, 3, 10);
//        warrior.engage(dummyEnemy);
//        assertTrue(dummyEnemy.getHealth().getAmount() < 100 || warrior.getHealth().getAmount() < 300);
//    }
//
//    @Test
//    void accept() {
//        warrior.setPosition(new Position(0,0));
//        dummyTrap.setPosition(new Position(0,1));
//        dummyTrap.interact(warrior);
//        // Should either engage or skip depending on range
//        assertTrue(warrior.getHealth().getAmount() <= warrior.getHealth().getPool());
//    }
//
//    @Test
//    void visit() {
//        Empty emptyTile = new Empty(new Position(0, 1));
//        warrior.visit(emptyTile);
//        assertEquals(new Position(0, 1), warrior.getPosition());
//    }
//
//    @Test
//    void testVisit() {
//        Wall wallTile = new Wall(new Position(0, 2));
//        Position before = warrior.getPosition();
//        warrior.visit(wallTile);
//        assertEquals(before, warrior.getPosition());
//    }
//
//    @Test
//    void testVisit1() {
//        Trap trap = new Trap('T', new Position(0, 1), 100, 10, 2, 1, 3);
//        Position before = warrior.getPosition();
//        trap.visit(warrior);
//        assertEquals(before, warrior.getPosition()); // Movement should not occur
//    }
//
//    @Test
//    void testVisit2() {
//        Enemy enemy = new Monster('M', new Position(0, 1), "Orc", 80, 10, 5, 3, 25);
//        Position before = warrior.getPosition();
//        warrior.visit(enemy);
//        // Warrior should engage in combat, possibly moving to the enemy's location
//        assertTrue(warrior.getPosition().equals(enemy.getPosition()) || warrior.getHealth().getAmount() < warrior.getHealth().getPool());
//    }
//
//    @Test
//    void getTile() {
//        assertEquals('@', warrior.getTile());
//    }
//
//    @Test
//    void testToString() {
//        assertEquals("@", warrior.toString());
//    }
//
//    @Test
//    void abilityReady() {
//        assertTrue(rogue.abilityReady());
//        rogue.castAbility();
//        assertFalse(rogue.abilityReady());
//    }
//}
