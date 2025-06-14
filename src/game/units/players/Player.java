package game.units.players;

import game.board.GameContext;
import game.units.HeroicUnit;
import game.units.enemies.Enemy;
import game.messages.MoveResult;
import game.tiles.*;

public abstract class Player extends Unit implements InteractionVisitor, HeroicUnit {

    protected int experience;
    protected int level;

    public Player(char tile, String name, int healthPool, int attack, int defense) {
        super(tile, name, healthPool, attack, defense);
        this.experience = 0;
        this.level = 1;
    }

    public abstract void tick();

    public void gainExperience(int amount) {
        experience += amount;
        while (experience >= level * 50) {
            levelUp();
        }
    }

    public void engage(Enemy enemy) {
        this.attack(enemy);// From Unit
        if (!enemy.isAlive()) {
            this.gainExperience(enemy.getExperienceValue());
        }
    }

    public MoveResult accept(InteractionVisitor visitor) {
        return visitor.visit(this); // calls visitor.visit(Player), or the most specific overload
    }

    public void levelUp() {
        experience -= level * 50;
        level++;
        healthPool += 10 * level;
        currentHealth = healthPool;
        attack += 4 * level;
        defense += level;
    }

    @Override
    public MoveResult visit(Empty empty) {
        this.swapPosition(empty);
        return MoveResult.moveTo(pos);
    }

    @Override
    public MoveResult visit(Wall wall) {
        return MoveResult.noMove(name + " tried to walk into a wall at " + wall.getPos() + " I suggest less drinking before exploring...");
    }

    @Override
    public char getTile(){
        return isAlive() ? '@' : 'X';
    }

    @Override
    public MoveResult visit(Enemy enemy) {
        //enemy.attack(this);
        this.engage(enemy);
        if (!enemy.isAlive()) {
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
        return MoveResult.noMove(name + " bumped into " + player.getName() + " Stay Alert!");
    }

    @Override
    public String toString() {
        return String.format("%s [Level: %d, XP: %d, HP: %d/%d, ATK: %d, DEF: %d]",
                name, level, experience, currentHealth, healthPool, attack, defense);
    }

    @Override
    abstract public boolean abilityReady(GameContext context); // To be implemented by subclasses
}
