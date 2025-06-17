package game.units.players;

import game.Position;
import game.board.GameContext;
import game.messages.MoveResult;
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
        healthPool += 5 * level;
        attack += 2 * level;
        defense += level;
    }

    @Override
    public String toString() {
        return String.format("Warrior %s [Level: %d, XP: %d, HP: %d/%d, ATK: %d, DEF: %d, Cooldown: %d/%d]",
                name, level, experience, currentHealth, healthPool, attack, defense, remainingCooldown, abilityCooldown);
    }

    public MoveResult castAbility(GameContext context) {
        if(!abilityReady(context))
            return MoveResult.abilityCasting(false,"Ability not ready.", new ArrayList<>(), false);

        StringBuilder msg = new StringBuilder();
        msg.append(name).append(" used Avenger's Mighty Shield bash!").append("\n");
        MoveResult result;
        List<Position> deadEnemies = new ArrayList<>();
        remainingCooldown = abilityCooldown;
        currentHealth = Math.min(currentHealth + 10 * defense, healthPool);
        List<Enemy> inRange = context.getEnemiesInRange(range);
        if (inRange.isEmpty()) {
            result = MoveResult.abilityCasting(false, "No enemies in range, healed for " + (10 * defense), null,true);
            return result;
        }
        Enemy target = inRange.get((int)(Math.random() * inRange.size()));
        int damage = healthPool / 10;
        msg.append(target.getName()).append(" received").append(damage).append(" damage").append("\n");
        target.receiveDamage(damage);

        if (!target.isAlive()) {
            msg.append(target.getName()).append(" is dead").append("\n").append(" gained ").append(target.getExperienceValue()).append(" EXP").append("\n");
            gainExperience(target.getExperienceValue());
            deadEnemies.add(target.getPos());
            result = MoveResult.abilityCasting(true, msg.toString(), deadEnemies,true);
            result.setPrint(true);
            return result;
        }
        result = MoveResult.abilityCasting(false, msg.toString(),deadEnemies,true);
        result.setPrint(true);
        return result;
    }
    public boolean abilityReady(GameContext context) {
        return remainingCooldown <= 0;
    }

    public int getAbilityCooldown() {
        return abilityCooldown;
    }

    public int getRemainingCooldown() {
        return remainingCooldown;
    }

}
