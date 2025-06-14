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

    @Override
    public MoveResult accept(InteractionVisitor visitor) {
        return visitor.visit(this); // calls visit(Enemy)
    }

    @Override
    public MoveResult visit(Empty empty) {
        this.swapPosition(empty);
        return MoveResult.moveTo(empty.getPos());
    }

    @Override
    public MoveResult visit(Player player) {
        attack(player);
        if (!this.isAlive()) {
            player.gainExperience(this.getExperienceValue());
            return MoveResult.noMove(this.getName()+ " was slain by the player");
        } else if (!player.isAlive()) {
            return MoveResult.noMove("You died. Next time think carefully before engaging in combat.");
        }

        return MoveResult.noMove("Engaged in combat with:" + this.getName());
    }

    @Override
    public MoveResult visit(Enemy otherEnemy) {
        return MoveResult.noMove(name + " and " + otherEnemy.getName() + " exchanged a secret handshake... what are they plotting?");
    }

    @Override
    public boolean isEnemy(){
        return true; }

    @Override
    public MoveResult visit(Wall wall) {

        return MoveResult.noMove("This Monster needs a pair glasses...");
    }

    public abstract Position onEnemyTurn(Player player);
}
