package game.players;

import game.Position;

public class Warrior extends Player {
    protected int abilityCooldown;
    protected int remainingCooldown;

    public Warrior(char tile, String name, int healthPool, int attack, int defense, int abilityCooldown, int remainingCooldown) {
        super(tile, name, healthPool, attack, defense);
        this.abilityCooldown = abilityCooldown;
        this.remainingCooldown = remainingCooldown;
    }

    public void warriorLevelUp() {
        remainingCooldown = 0;
        healthPool += 5 * level;
        attack += 2 * level;
        defense += 1 * level;
        super.levelUp();
    }
    @Override
    public String toString() {
        return String.format("Warrior %s [Level: %d, XP: %d, HP: %d/%d, ATK: %d, DEF: %d, Cooldown: %d/%d]",
                Name, level, experience, currentHealth, healthPool, attack, defense, remainingCooldown, abilityCooldown);
    }

}
