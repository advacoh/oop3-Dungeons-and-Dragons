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

       List<Position> deadEnemies = new ArrayList<>();
       List<Enemy> enemiesInRange = context.getEnemiesInRange(2);
       currentEnergy -= cost;

       for (Enemy enemy: enemiesInRange) {
           int damage = attack - (int)(Math.random() * enemy.getDefense());
           enemy.receiveDamage(damage);

           if (!enemy.isAlive()) {
               deadEnemies.add(enemy.getPos());
               gainExperience(enemy.getExperienceValue());

           }
       }
       return MoveResult.abilityCasting(true, "Rogue attacked enemies in range for " + attack + " damage", deadEnemies, true);
    }
}
