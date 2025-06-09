package game.players;

import game.messages.MoveResult;
import game.tiles.Unit;

public class Warrior extends Player {
    protected int abilityCooldown;
    protected int remainingCooldown;

    public Warrior(char tile, String name, int healthPool, int attack, int defense, int abilityCooldown) {
        super(tile, name, healthPool, attack, defense);
        this.abilityCooldown = abilityCooldown;
        this.remainingCooldown = 0;
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


}
