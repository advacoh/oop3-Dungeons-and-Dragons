package game.tiles;
import game.Position;
import game.messages.MoveResult;

public abstract class Unit extends Tile {
    protected String Name;
    protected int HealthPool;
    protected int currentHealth;
    protected int Attack;
    protected int Defense;

    public Unit(char tile, String name, int healthPool, int attack, int defense) {
        super(tile);
        this.Name = name;
        this.HealthPool = healthPool;
        this.currentHealth = healthPool; // Start with full health
        this.Attack = attack;
        this.Defense = defense;
    }
    public String getName() {
        return Name;
    }

    public void setPos(Position position) {
        this.pos = position;
    }

    protected void swapPosition(Tile other) {
        Position myPos = this.getPos();
        this.setPos(other.getPos());
        other.setPos(myPos);
    }

    public void attack(Unit target) {
        int damage = Math.max(0, this.Attack - target.Defense);
        target.receiveDamage(damage);
        System.out.printf("%s attacks %s for %d damage!\n", this.Name, target.Name, damage);
    }

    public void die() {
        System.out.println(Name + " has died.");
        currentHealth = 0;
        // Additional logic for player death can be added here
    }

    public boolean isAlive() {
        return currentHealth > 0;
    }

    public void receiveDamage(int damage) {
        currentHealth = Math.max(0, currentHealth - damage);
        if (currentHealth == 0) {
            System.out.printf("%s has died.\n", Name);
        }
    }

    @Override
    public boolean isUnit() {
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s [HP: %d/%d, ATK: %d, DEF: %d]", Name, currentHealth, HealthPool, Attack, Defense);
    }

    @Override
    public MoveResult interact(Tile actor){
        return actor.accept((InteractionVisitor) this);
    }
    public int getDefense() {
        return Defense;
    }
}
