package game.units.enemies;

import game.Position;
import game.messages.MoveResult;
import game.tiles.*;
import game.units.players.Player;

public abstract class Enemy extends Unit implements InteractionVisitor {

    protected int experienceValue;

    public Enemy(char tile, String name, int healthPool, int attack, int defense, int experienceValue) {
        super(tile, name, healthPool, attack, defense);
        this.experienceValue = experienceValue;
    }

    public int getExperienceValue() {
        return experienceValue;
    }

    // Accept interaction (e.g. when a Player visits us)
    @Override
    public MoveResult accept(InteractionVisitor visitor) {
        return visitor.visit(this); // calls visit(Enemy)
    }

    // Interaction behavior with Tiles
    @Override
    public MoveResult visit(Empty empty) {
        this.swapPosition(empty);
        System.out.println(Name + " moved to " + empty.getPos());
        return MoveResult.moveTo(empty.getPos());
    }

    @Override
    public MoveResult visit(Player player) {
        System.out.println(Name + " engages in combat with " + player.getName());
        //player.engage(this);
        attack(player);
        if (!this.isAlive()) {
            System.out.println(player.getName() + " defeated " + this.getName() + " and gained EXP.");
            player.gainExperience(this.getExperienceValue());
            return MoveResult.noMove(this.getName()+ " was slain by the player");
        } else if (!player.isAlive()) {
            return MoveResult.noMove("You died. Next time think carefully before engaging in combat.");
        }
        return MoveResult.noMove("Engaged in combat with:" + this.getName());
    }

    @Override
    public MoveResult visit(Enemy otherEnemy) {
        return MoveResult.noMove(Name + " and " + otherEnemy.getName() + " exchanged a secret handshake... what are they plotting?");
    }

    @Override
    public MoveResult visit(Wall wall) {
        return MoveResult.noMove("This Monster needs a pair glasses...");
    }

    public abstract Position onEnemyTurn(Player player); // Called each tick
}
