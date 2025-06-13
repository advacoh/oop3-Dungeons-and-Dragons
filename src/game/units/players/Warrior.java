package game.units.players;

import game.Position;
import game.board.GameContext;
import game.messages.MoveResult;
import game.units.HeroicUnit;
import game.units.enemies.Enemy;

import java.util.ArrayList;
import java.util.List;

public class Warrior extends Player {
    protected int abilityCooldown;
    protected int remainingCooldown;
    private final int range = 3;

    public Warrior(char tile, String name, int healthPool, int attack, int defense, int abilityCooldown) {
        super(tile, name, healthPool, attack, defense);
        this.abilityCooldown = abilityCooldown;
        this.remainingCooldown = 0;
    }

    @Override
    public void tick(){
        if (remainingCooldown > 0) {
            remainingCooldown--;
        }
    }

    @Override
    public void levelUp() {
        super.levelUp(); // applies base level up bonuses
        remainingCooldown = 0;
        HealthPool += 5 * level;
        Attack += 2 * level;
        Defense += 1 * level;
    }

    @Override
    public String toString() {
        return String.format("Warrior %s [Level: %d, XP: %d, HP: %d/%d, ATK: %d, DEF: %d, Cooldown: %d/%d]",
                Name, level, experience, currentHealth, HealthPool, Attack, Defense, remainingCooldown, abilityCooldown);
    }

    public MoveResult castAbility(GameContext context) {
        List<Position> deadEnemies = new ArrayList<>();
        remainingCooldown += abilityCooldown;
        List<Enemy> inRange = context.getEnemiesInRange(range);
        if (inRange.isEmpty()) {
            return MoveResult.abilityCasting(false, "No enemies in range, healed for " + (10 * Defense), null,true);
        }
        Enemy target = inRange.get((int)(Math.random() * inRange.size()));
        int damage = HealthPool / 10;
        currentHealth += 10 * Defense;
        target.receiveDamage(damage);

        if (!target.isAlive()) {
            gainExperience(target.getExperienceValue());
            deadEnemies.add(target.getPos());
            return MoveResult.abilityCasting(true, "Attacked " + target.getName() + " for " + damage + " damage and healed for " + (10 * Defense), deadEnemies,true);
        }
        return MoveResult.abilityCasting(false, "Attacked " + target.getName() + " for " + damage + " damage and healed for " + (10 * Defense),deadEnemies,true);
    }
    public boolean abilityReady(GameContext context) {
        return remainingCooldown <= 0;

    }

}
