package game.units.players;

import game.Position;
import game.board.GameContext;
import game.messages.MoveResult;
import game.units.enemies.Enemy;

import java.util.ArrayList;
import java.util.List;

public class Rogue extends Player {
    protected int cost;
    protected int currentEnergy;

    public Rogue(char tile, String name, int healthPool, int attack, int defense,int cost) {
        super(tile, name, healthPool, attack, defense);
        this.cost = cost;
        this.currentEnergy = 100;
    }

    @Override
    public void levelUp() {
        super.levelUp(); // applies base level up bonuses
        currentEnergy = 100;
        attack += 3 * level;
    }

    @Override
    public String toString() {
        return String.format("Rogue %s [Level: %d, XP: %d, HP: %d/%d, ATK: %d, DEF: %d, Energy: %d/100, Cost: %d]",
                name, level, experience, currentHealth, healthPool, attack, defense, currentEnergy, cost);
    }

    @Override
    public void tick() {
        currentEnergy = Math.min(currentEnergy + 10, 100);
    }

    public boolean abilityReady(GameContext context) {
        return currentEnergy >= cost && !context.getEnemiesInRange(2).isEmpty();
    }

    public MoveResult castAbility(GameContext context) {
        StringBuilder msg = new StringBuilder();
        MoveResult result;
        boolean hasdied = false;
        msg.append(name).append(" has casted Fan of Knives!\n");
       List<Position> deadEnemies = new ArrayList<>();
       List<Enemy> enemiesInRange = context.getEnemiesInRange(2);
       currentEnergy -= cost;

       for (Enemy enemy: enemiesInRange) {
           int defroll = (int)(Math.random() * enemy.getDefense());
           msg.append(enemy.getName()).append(" has rolled ").append(defroll).append(" defense!");
           int damage = attack - defroll;
           enemy.receiveDamage(damage);
           msg.append("\n").append(enemy.getName()).append(" received ").append(damage).append(" damage");

           if (!enemy.isAlive()) {
               msg.append("\n").append(enemy.getName()).append(" was defeated").append("\n").append(name).append(" gained ").append(enemy.getExperienceValue()).append(" EXP");
               deadEnemies.add(enemy.getPos());
               gainExperience(enemy.getExperienceValue());
                hasdied = true;
           }
       }
       result = MoveResult.abilityCasting(true, msg.toString(), deadEnemies, true);
       result.setPrint(true);
       result.setHasMoved(hasdied);
       return result;
    }
}
