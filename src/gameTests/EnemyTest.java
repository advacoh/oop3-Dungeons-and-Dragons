package gameTests;

import game.Position;
import game.board.Board;
import game.tiles.Tile;
import game.units.enemies.*;
import game.units.players.Warrior;
import game.tiles.Wall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class EnemyTest {

    private Path blankLevel;            // 5×5 dots

    @BeforeEach
    void makeLevel() throws Exception {
        String dots = ".....%n".repeat(5);
        blankLevel = Files.createTempFile("lvl",".txt");
        Files.writeString(blankLevel, String.format(dots));
    }
    private Board boardWith(Warrior p,List<Enemy> es)throws Exception{
        Board b= Board.getInstance(p,blankLevel.toString());
        b.setEnemies(es);
        return b;
    }

    /* -------------------------------- experience ----------------------------- */
    @Test
    void experienceValues() {
        assertEquals(25 , new Monster("Soldier",'s',80,8,3,3,25,new Position(0,0))
                .getExperienceValue());
        assertEquals(250, new Trap("Bonus",'B',1,1,1,250,1,5,new Position(0,0))
                .getExperienceValue());
    }


    /* -------------------------- trap visibility ------------------------------ */
    @Test
    void trap_visibilityCycle() {
        int vis = 2, invis = 3;
        Trap trap = new Trap("Bonus", 'B', 1, 3, 1, 250,
                vis, invis, new Position(0, 0));
        Warrior dummy = new Warrior('@', "D", 10, 1, 1, 1);
        dummy.setPos(new Position(4, 4));

        boolean phaseState   = trap.isVisible();
        int     ticksInPhase = 0;
        int     flipsSeen    = 0;

        while (flipsSeen < 2) {
            boolean before = trap.isVisible();
            trap.onEnemyTurn(dummy);
            ticksInPhase++;
            boolean after  = trap.isVisible();

            if (before != after) {
                int expected = before ? vis : invis;
                assertEquals(expected, ticksInPhase,
                        "Wrong phase length before flip #" + flipsSeen);

                // start next phase
                phaseState   = after;
                ticksInPhase = 0;
                flipsSeen++;
            }
        }
    }
    /* -------------------------- trap attacks in place ------------------------ */
    @Test
    void trap_reducesPlayerHP_whenInRange() throws Exception {
        Warrior hero = new Warrior('@',"Jon",100,0,0,1); // DEF=0
        hero.setPos(new Position(1,0));                 // melee range

        Trap killer = new Trap("Killer",'K',
                250,1000,0,100,
                3,7,new Position(0,0));
        boardWith(hero,List.of(killer));

        int start = hero.getHealthPool();
        for(int i=0;i<15 && hero.getHealthPool()==start;i++) {
            killer.onEnemyTurn(hero);
            killer.interact(hero);
        }

        assertTrue(hero.getHealthPool() < start,
                "Player HP should drop after ≤15 trap turns");
        /* ensure trap never moved */
        assertPosEquals(new Position(0,0), killer.getPos());
    }

    /* -------------------------- visitor: wall -------------------------------- */
    @Test
    void monster_visitWall_stays() {
        Monster m = new Monster("Soldier",'s',80,8,3,3,25,new Position(0,0));
        m.visit(new Wall(new Position(0,1)));
        assertPosEquals(new Position(0,0), m.getPos());
    }

    /* ----------------------------- helpers ----------------------------------- */
    private static double dist(Position a,Position b){
        int dx=a.getX()-b.getX(),dy=a.getY()-b.getY();
        return Math.sqrt(dx*dx+dy*dy);
    }
    private static void assertPosEquals(Position e,Position a){
        assertEquals(e.getX(),a.getX());
        assertEquals(e.getY(),a.getY());
    }
}