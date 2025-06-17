package game.units.enemies;

import game.Position;
import game.messages.MoveResult;
import game.units.players.Player;


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
    public char getTile() {
        return visible ? tile : '.';
    }

    @Override
    public Position onEnemyTurn(Player player) {
        visible = isVisible();
        ticksCount++;
        if (ticksCount == visibilityTime + invisibilityTime) {
            ticksCount = 0;
        }
        if (player.getPos().range(pos.x, pos.y) < 2) {
            return player.getPos();
        }
        return pos;
    }

    public boolean isVisible() {
        return ticksCount < visibilityTime;
    }

    @Override
    public MoveResult visit(Enemy enemy){
        return MoveResult.noMove("the Trap is Trapping.");
    }
}
