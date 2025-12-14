package com.pablo.pablosmod.level;

public class PlayerWoodData {

    private int woodLevel = 0;
    private int woodXp = 0;
    private static final int BASE_XP_PER_LEVEL = 100;
    private static final int XP_INCREMENT = 30;

    public void addXp(int amount) {
        woodXp += amount;
        while (woodXp >= getXpRequiredForLevel(woodLevel)) {
            woodXp -= getXpRequiredForLevel(woodLevel);
            woodLevel++;
        }
    }

    private int getXpRequiredForLevel(int currentLevel) {
        return BASE_XP_PER_LEVEL + (currentLevel - 1) * XP_INCREMENT;
    }

    public int getWoodLevel() {
        return woodLevel;
    }

    public int getWoodXp() {
        return woodXp;
    }

    public void setWoodLevel(int level) {
        this.woodLevel = level;
    }

    public void setWoodXp(int xp) {
        this.woodXp = xp;
    }

    public int getTotalXpForNextLevel() {
        return getXpRequiredForLevel(woodLevel);
    }

    public double getEfficiencyBonus() {
        int efficiencyLevels = woodLevel - (woodLevel / 5);
        return efficiencyLevels * 0.01;
    }

    public double getFortuneBonus() {
        int fortuneLevels = woodLevel / 5;
        return fortuneLevels * 0.01;
    }
}
