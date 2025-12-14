package com.pablo.pablosmod.level;

public class PlayerAgilityData {

    private int agilityLevel = 0;
    private int agilityXp = 0;
    private static final int BASE_XP_PER_LEVEL = 500;
    private static final int XP_INCREMENT = 200;

    public void addXp(int amount) {
        agilityXp += amount;
        while (agilityXp >= getXpRequiredForLevel(agilityLevel)) {
            agilityXp -= getXpRequiredForLevel(agilityLevel);
            agilityLevel++;
        }
    }

    private int getXpRequiredForLevel(int currentLevel) {
        return BASE_XP_PER_LEVEL + (currentLevel - 1) * XP_INCREMENT;
    }

    public int getAgilityLevel() {
        return agilityLevel;
    }

    public int getAgilityXp() {
        return agilityXp;
    }

    public void setAgilityLevel(int level) {
        this.agilityLevel = level;
    }

    public void setAgilityXp(int xp) {
        this.agilityXp = xp;
    }

    public int getTotalXpForNextLevel() {
        return getXpRequiredForLevel(agilityLevel);
    }

    public double getSpeedBonus() {
        int speedLevels = agilityLevel - (agilityLevel / 5);
        return Math.min(speedLevels * 0.001, 0.015);

    }

    public double getJumpBonus() {
        int jumpLevels = agilityLevel / 5;
        return Math.min(jumpLevels * 0.01, 0.02);
    }

    public double getFallReduction() {
        return Math.min(agilityLevel * 0.005, 0.2);
    }
}
