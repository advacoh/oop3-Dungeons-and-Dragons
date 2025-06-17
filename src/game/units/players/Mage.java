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
        if(!abilityReady(context))
            return MoveResult.abilityCasting(false,"Ability not ready.", new ArrayList<>(), false);
        MoveResult result;
        boolean hasdied = false;
        StringBuilder msg = new StringBuilder();
        msg.append(name).append(" has casted Blizzard!");
        int hits = 0;
        currentMana -= manaCost;

        List<Enemy> enemiesInRange = context.getEnemiesInRange(abilityRange);
        List<Position> deadEnemies = new ArrayList<>();
        while (hits < hitsCount && !enemiesInRange.isEmpty()) {
            Enemy target = enemiesInRange.get((int)(Math.random() * enemiesInRange.size()));
            int defroll = (int)(Math.random() * target.getDefense());
            int damage = spellPower - defroll;
            msg.append("\n").append(target.getName()).append(" has rolled ").append(defroll).append(" defense");
            msg.append("\n").append(target.getName()).append(" received ").append(damage).append(" damage");
            target.receiveDamage(damage);
            hits++;

            if (!target.isAlive()) {
                msg.append("\n").append(target.getName()).append(" was wiped out by the blizzard!").append("\n").append(target.getExperienceValue()).append(" EXP was gained!");
                deadEnemies.add(target.getPos());
                gainExperience(target.getExperienceValue());
                enemiesInRange.remove(target);
                hasdied = true;
            }

        }
        result = MoveResult.abilityCasting(true, msg.toString(), deadEnemies, true);
        result.setPrint(true);
        result.setHasMoved(hasdied);
        return result;
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

    public int getCurrentMana() {
        return currentMana;
    }

    public void setCurrentMana(int i) {
        currentMana = i;
    }

    public int getManaPool() {
        return manaPool;
    }
}
