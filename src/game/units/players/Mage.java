package game.units.players;

import game.Position;
import game.board.GameContext;
import game.messages.MoveResult;
import game.units.enemies.Enemy;
import java.util.ArrayList;
import java.util.List;

public class Mage extends Player {
    protected int manaPool;
    protected int currentMana;
    protected int manaCost;
    protected int spellPower;
    protected int hitsCount;
    protected int abilityRange;

    public Mage(char tile, String name, int healthPool, int attack, int defense,int manaPool, int manaCost, int spellPower, int hitsCount, int abilityRange) {
        super(tile, name, healthPool, attack, defense);
        this.manaPool = manaPool;
        this.currentMana = manaPool / 4;
        this.manaCost = manaCost;
        this.spellPower = spellPower;
        this.hitsCount = hitsCount;
        this.abilityRange = abilityRange;
    }

    @Override
    public void levelUp() {
        super.levelUp();
        manaPool += 25 * level;
        currentMana = Math.min(currentMana +  manaPool / 4, manaPool);
        spellPower += 10 * level;
    }

    @Override
    public void tick() {
        currentMana = Math.min(currentMana + level, manaPool);
    }
    @Override
    public MoveResult castAbility(GameContext context) {
        int hits = 0;
        currentMana -= manaCost;

        List<Enemy> enemiesInRange = context.getEnemiesInRange(abilityRange);
        List<Position> deadEnemies = new ArrayList<>();
        while (hits < hitsCount && !enemiesInRange.isEmpty()) {
            Enemy target = enemiesInRange.get((int)(Math.random() * enemiesInRange.size()));
            int damage = spellPower - (int)(Math.random() * target.getDefense());
            target.receiveDamage(damage);
            hits++;

            if (!target.isAlive()) {
                deadEnemies.add(target.getPos());
                gainExperience(target.getExperienceValue());
                enemiesInRange.remove(target); // Remove dead enemy from the list
            }

        }
        return MoveResult.abilityCasting(true, "Casted spell on " + hits + " enemies for " + (spellPower * hits) + " total damage.", deadEnemies, true);
    }

    @Override
    public boolean abilityReady(GameContext context) {
        List<Enemy> enemiesInRange = context.getEnemiesInRange(abilityRange);

        return currentMana >= manaCost && !enemiesInRange.isEmpty();
    }

    @Override
    public String toString() {
        return String.format("Mage %s [Level: %d, XP: %d, HP: %d/%d, ATK: %d, DEF: %d, Mana: %d/%d, Spell Power: %d, Hits: %d, Range: %d]",
                name, level, experience, currentHealth, healthPool, attack, defense, currentMana, manaPool, spellPower, hitsCount, abilityRange);
    }
}
