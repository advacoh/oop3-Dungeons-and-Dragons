package game.tiles;
import game.Position;

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

    @Override
    public String toString() {
        return String.format("%s [HP: %d/%d, ATK: %d, DEF: %d]", Name, currentHealth, HealthPool, Attack, Defense);
    }
}
