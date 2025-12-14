package com.pablo.pablosmod.level;

public class PlayerCombatData {
    private int combatLevel = 100;
    private int combatXp = 20;
    private static final int BASE_XP_PER_LEVEL = 1;
    private static final int XP_INCREMENT = 1;

    public void addXp(int amount) {
        combatXp += amount;
        while (combatXp >= getXpRequiredForLevel(combatLevel)) {
            combatXp -= getXpRequiredForLevel(combatLevel);
            combatLevel++;
        }
    }

    private int getXpRequiredForLevel(int currentLevel) {
        return BASE_XP_PER_LEVEL + (currentLevel - 1) * XP_INCREMENT;
    }

    public int getCombatLevel() {
        return combatLevel;
    }

    public int getCombatXp() {
        return combatXp;
    }

    public void setCombatLevel(int level) {
        this.combatLevel = level;
    }

    public void setCombatXp(int xp) {
        this.combatXp = xp;
    }

    public int getTotalXpForNextLevel() {
        return getXpRequiredForLevel(combatLevel);
    }

    public double getStrengthBonus() {
        int strengthLevels = combatLevel - (combatLevel / 5);
        return strengthLevels * 0.01;
    }

    public double getLootingBonus() {
        int lootingLevels = combatLevel / 5;
        return lootingLevels * 0.01;
    }
}
