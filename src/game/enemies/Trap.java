package game.enemies;

import game.Position;
import game.messages.MoveResult;
import game.players.Player;
import game.tiles.Tile;
import game.tiles.Unit;

public class Trap extends Enemy {

    protected int visibilityTime;
    protected int invisibilityTime;
    protected int ticksCount;
    protected boolean visible;

    public Trap(String name, char tile, int healthPool, int attack, int defense, int experienceValue, int visibilityTime, int invisibilityTime, Position pos) {
        super(tile, name, healthPool, attack, defense, experienceValue);
        this.visibilityTime = visibilityTime;
        this.invisibilityTime = invisibilityTime;
        this.ticksCount = 0;
        this.visible = true;
        this.pos = pos;
    }

    @Override
    public Position onEnemyTurn(Player player) {
        if (ticksCount == visibilityTime + invisibilityTime) {
            ticksCount = 0;
        } else {
            ticksCount++;
        }

        visible = ticksCount < visibilityTime;

        if (this.pos.range(player.getPos().getX(), player.getPos().getY()) < 2) {
            attack(player); // Use inherited Unit method
        }
        return pos;
    }

    public boolean isVisible() {
        return visible;
    }

    @Override
    public MoveResult interact(Tile unit) {
        return MoveResult.noMove("A Trap is currently stopping you from moving!");
    }
}
