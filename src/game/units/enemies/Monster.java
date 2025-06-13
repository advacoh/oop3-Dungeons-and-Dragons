package game.units.enemies;

import game.Position;
import game.units.players.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class Monster extends Enemy {

    protected int visionRange;
    private Position homePos;
    private final int patrolRange;
    private int ticksSinceHomeRefresh;

    public Monster(String name, char tile, int healthPool, int attack, int defense,
                   int visionRange, int experienceValue, Position spawnPos, int patrolRange) {
        super(tile, name, healthPool, attack, defense, experienceValue);
        this.visionRange = visionRange;
        this.homePos = spawnPos;
        this.patrolRange = patrolRange;
        this.setPos(spawnPos);
        this.ticksSinceHomeRefresh = 0;
    }

    public Monster(String name, char tile, int healthPool, int attack, int defense,
                   int visionRange, int experienceValue, Position spawnPos) {
        super(tile, name, healthPool, attack, defense, experienceValue);
        this.visionRange = visionRange;
        this.homePos = spawnPos;
        this.patrolRange = 5;
        this.setPos(spawnPos);
        this.ticksSinceHomeRefresh = 0;
    }

    private boolean withinPatrol(Position target) {
        return homePos.range(target.getX(), target.getY()) <= patrolRange;
    }


    @Override
    public Position onEnemyTurn(Player player) {
        Position playerPos = player.getPos();
        Position current = this.getPos();
        double distanceToPlayer = current.range(playerPos.getX(), playerPos.getY());

        ticksSinceHomeRefresh++;
        if (ticksSinceHomeRefresh >= patrolRange * 2) {
            homePos = getPos();
            ticksSinceHomeRefresh = 0;
        }

        Position moveTarget;

        if (distanceToPlayer < visionRange) {
            // Chase player (override patrol limit)
            int dx = current.getX() - playerPos.getX();
            int dy = current.getY() - playerPos.getY();

            if (Math.abs(dx) > Math.abs(dy)) {
                moveTarget = dx > 0 ? current.shiftBy('a') : current.shiftBy('d');
            } else {
                moveTarget = dy > 0 ? current.shiftBy('w') : current.shiftBy('s');
            }

            // Always allow chase move
            return moveTarget;

        } else {
            // Random move (must respect patrol limit)
            List<Character> dirs = new ArrayList<>(Arrays.asList('w', 'a', 's', 'd'));
            Collections.shuffle(dirs);
            dirs.add('x'); //

            for (char dir : dirs) {
                moveTarget = dir == 'x' ? current : current.shiftBy(dir);
                if (withinPatrol(moveTarget)) {
                    return moveTarget;
                }
            }
        }
        return this.pos;
    }

}
