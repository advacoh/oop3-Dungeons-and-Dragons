package game.players;

import game.messages.MoveResult;
import game.tiles.*;
import game.enemies.Enemy;
import game.tiles.Unit;
import game.tiles.InteractionVisitor;
import game.Position;

public abstract class Player extends Unit implements InteractionVisitor {

    protected int experience;
    protected int level;

    public Player(char tile, String name, int healthPool, int attack, int defense) {
        super(tile, name, healthPool, attack, defense);
        this.experience = 0;
        this.level = 1;
    }

    public void gainExperience(int amount) {
        experience += amount;
        if (experience >= level * 50) {
            levelUp();
        }
    }

    public void engage(Enemy enemy) {
        this.attack(enemy);// From Unit
        if (!enemy.isAlive()) {
            this.gainExperience(enemy.getExperienceValue());
            System.out.println(this.Name + " gained " + enemy.getExperienceValue() + " XP!");
        }
    }

    public MoveResult accept(InteractionVisitor visitor) {
        return visitor.visit(this); // calls visitor.visit(Player), or the most specific overload
    }

    public void levelUp() {
        experience -= level * 50;
        level++;
        HealthPool += 10 * level;
        currentHealth = HealthPool;
        Attack += 4 * level;
        Defense += 1 * level;
    }

    public void abilityAttempt() {
        if (currentHealth <= 0) {
            System.out.println("You cannot use abilities while dead.");
        }
    }

    @Override
    public MoveResult visit(Empty empty) {
        this.swapPosition(empty);
        return MoveResult.moveTo(pos);
    }

    @Override
    public MoveResult visit(Wall wall) {
        return MoveResult.noMove(Name + " tried to walk into a wall at " + wall.getPos() + " I suggest less drinking before exploring...");
    }

    @Override
    public MoveResult visit(Enemy enemy) {
        System.out.println(Name + " engages in combat with " + enemy.getName());
        enemy.attack(this);
        this.engage(enemy);
        if (!enemy.isAlive()) {
            System.out.println(Name + " defeated " + enemy.getName() + " and gained EXP.");
            this.gainExperience(enemy.getExperienceValue());
            setPos(enemy.getPos());
            return MoveResult.moveToWithDefeat(enemy.getPos(), enemy);
        } else if (currentHealth == 0) {
            return MoveResult.noMove("You died. Next time think carefully before engaging in combat.");
        }
        return MoveResult.noMove("Engaged in combat with:" + enemy.getName());
    }

    @Override
    public MoveResult visit(Player player) {
        return MoveResult.noMove(Name + " bumped into " + player.getName() + " Stay Alert!");
    }


    @Override
    public String toString() {
        return String.format("%s [Level: %d, XP: %d, HP: %d/%d, ATK: %d, DEF: %d]",
                Name, level, experience, currentHealth, HealthPool, Attack, Defense);
    }
}
