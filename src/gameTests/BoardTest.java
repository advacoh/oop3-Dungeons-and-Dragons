package gameTests;

import game.Position;
import game.board.Board;
import game.tiles.Tile;
import game.tiles.Empty;
import game.tiles.Wall;
import game.units.enemies.Monster;
import game.units.players.Player;
import game.units.players.Warrior;
import org.junit.jupiter.api.*;

import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 *  Basic smoke-tests for Board loading & movement.
 */
class BoardTest {

    /** 3×3 level:
     *  #@.   (# wall, @ player spawn, . floor)
     *  .#.   (central # blocks movement)
     *  ... */
    private static final String MINI_LVL =
            "#@.\n" +
                    ".#.\n" +
                    "...\n";

    private Path levelFile;

    @BeforeEach
    void writeTempLevel() throws Exception {
        levelFile = Files.createTempFile("mini", ".txt");
        Files.writeString(levelFile, MINI_LVL);
    }

    /** Helper that gives us a fresh board with Jon Snow in it. */
    private Board newBoard() throws Exception {
        Warrior jon = new Warrior('@', "Jon", 100, 10, 3, 1);
        return Board.getInstance(jon, levelFile.toString());
    }

    /* ---------------------------------------------------------------------- */
    /* 1.  Level loads and inBounds() works                                   */
    /* ---------------------------------------------------------------------- */
    @Test
    void loadsLevel_andInBoundsBehaves() throws Exception {
        Board b = newBoard();

        // corners inside
        assertTrue (b.inBounds(new Position(0,0)));
        assertTrue (b.inBounds(new Position(2,2)));
        // just outside
        assertFalse(b.inBounds(new Position(-1,0)));
        assertFalse(b.inBounds(new Position(0,3)));
    }

    /* ---------------------------------------------------------------------- */
    /* 2.  Player spawn tile is really ‘@’ and is returned by getTile()        */
    /* ---------------------------------------------------------------------- */
    @Test
    void playerPlaced_correctly() throws Exception {
        Board b = newBoard();
        // Player always handed to Board already has its pos set by the ctor
        Position pPos = b.getPlayer().getPos();
        Tile t       = b.getTile(pPos.getX(), pPos.getY());

        assertEquals('@', t.getTile(),
                "The tile where the player stands should contain ‘@’");
    }

    /* ---------------------------------------------------------------------- */
    /* 3-a.  Player can step onto a free floor tile                            */
    /* ---------------------------------------------------------------------- */
    @Test
    void playerMovesOntoFloor() throws Exception {
        Board b = newBoard();                 // @ is at (1,0)
        Position oldPos = b.getPlayer().getPos();

        b.tryMovePlayer('d');                 // move down onto '.'

        Position newPos = b.getPlayer().getPos();
        assertEquals(oldPos.shiftBy('d').toString(), newPos.toString(),
                "Player should have moved one step down");
        assertTrue(b.getTile(newPos.getX(), newPos.getY()) instanceof Player);
        b.printBoard();
    }

    /* ---------------------------------------------------------------------- */
    /* 3-b.  Player blocked by a wall (‘#’)                                    */
    /* ---------------------------------------------------------------------- */
    @Test
    void playerBlockedByWall() throws Exception {
        Board b = newBoard();                 // @ is at (1,0); wall at (0,0)

        Position before = b.getPlayer().getPos();
        b.tryMovePlayer('a');                 // try to move left into wall

        assertEquals(before.toString(), b.getPlayer().getPos().toString(),
                "Player should NOT have moved through a wall");
        assertTrue(b.getTile(0,0) instanceof Wall);
    }

    /* ---------------------------------------------------------------------- */
    /* 4.  setEnemies / getTile round-trip                                     */
    /* ---------------------------------------------------------------------- */
    @Test
    void enemyInserted_isRetrievable() throws Exception {
        Board b = newBoard();

        Monster z = new Monster("Soldier",'s',
                80,8,3,
                2,25,new Position(2,2));

        b.setEnemies(List.of(z));

        Tile t = b.getTile(2,2);
        assertEquals('s', t.getTile(),
                "Board should now show the monster tile at (2,2)");
    }
}