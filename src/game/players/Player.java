package game.players;

import game.Position;
import game.tiles.Unit;

public abstract class Player extends Unit {
    protected int experience;
    protected int level;
    protected int healthPool;
    protected int currentHealth;
    protected int attack;
    protected int defense;


    public Player(char tile, String name, int healthPool, int attack, int defense) {
        super(tile, name, healthPool, attack, defense);
        this.experience = 0;
        this.level = 1;
    }
    public void gainExperience(int amount) {
        experience += amount;
        if (experience >= level * 50) { // Example level-up condition
            levelUp();
        }

    }
    public void levelUp() {
        experience -= level * 50;
        level++;
        healthPool += 10 * level;
        currentHealth = healthPool;
        attack += 4 * level;
        defense += 1 * level;

    }

    public void abilityAttempt(){
        if (currentHealth <= 0) {
            System.out.println("You cannot use abilities while dead.");
            return;
        }
    }


    @Override
    public String toString() {
        return String.format("%s [Level: %d, XP: %d, HP: %d/%d, ATK: %d, DEF: %d]",
                Name, level, experience, currentHealth, healthPool, attack, defense);
    }
}
