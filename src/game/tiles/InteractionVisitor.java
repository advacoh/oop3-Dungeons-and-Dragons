package game.tiles;

import game.units.enemies.Enemy;
import game.messages.MoveResult;
import game.units.players.Player;

public interface InteractionVisitor {
    MoveResult visit(Player p);
    MoveResult visit(Enemy e);
    MoveResult visit(Wall w);
    MoveResult visit(Empty e);
}