package game.tiles;

import game.enemies.Enemy;
import game.messages.MoveResult;
import game.players.Player;

public interface InteractionVisitor {
    MoveResult visit(Player p);
    MoveResult visit(Enemy e);
    MoveResult visit(Wall w);
    MoveResult visit(Empty e);
}