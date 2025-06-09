package game.board;

import game.Position;
import game.messages.MoveResult;
import game.players.Player;
import game.tiles.*;

import java.util.List;
import java.util.stream.Stream;

public interface GameContext {
    Player getPlayer();
    Tile getTile(int x, int y);
    Stream<Tile> getTilesInRange(Position center, int range);
    boolean isWalkable(Position pos);
    void moveEnemy(Position from, Position to); // if needed
    public List<Unit> getUnitsAround(Position center, int range);

    }
