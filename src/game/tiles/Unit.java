package game.tiles;
import game.Position;
import game.messages.MoveResult;

public abstract class Unit extends Tile {
    protected String name;
    protected int healthPool;
    protected int currentHealth;
    protected int attack;
    protected int defense;

    public Unit(char tile, String name, int healthPool, int attack, int defense) {
        super(tile);
        this.name = name;
        this.healthPool = healthPool;
        this.currentHealth = healthPool; // Start with full health
        this.attack = attack;
        this.defense = defense;
    }
    public String getName() {
        return name;
    }

    protected void swapPosition(Tile other) {
        Position myPos = this.getPos();
        this.setPos(other.getPos());
        other.setPos(myPos);
    }

    public void attack(Unit target) {
        int damage = Math.max(0, this.attack - target.defense);
        target.receiveDamage(damage);
    }

    public boolean isAlive() {
        return currentHealth > 0;
    }

    public void receiveDamage(int damage) {
        currentHealth = Math.max(0, currentHealth - damage);
    }

    @Override
    public boolean isUnit() {
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s [HP: %d/%d, ATK: %d, DEF: %d]", name, currentHealth, healthPool, attack, defense);
    }

    @Override
    public MoveResult interact(Tile actor){
        return actor.accept((InteractionVisitor) this);
    }

    public int getDefense() {
        return defense;
    }
}
