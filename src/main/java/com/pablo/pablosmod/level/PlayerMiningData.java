package com.pablo.pablosmod.level;

public class PlayerMiningData {

    private int miningLevel = 100;
    private int miningXp = 20;
    private static final int BASE_XP_PER_LEVEL = 1;
    private static final int XP_INCREMENT = 1;

    public void addXp(int amount) {
        miningXp += amount;

        while (miningXp >= getXpRequiredForLevel(miningLevel)) {
            miningXp -= getXpRequiredForLevel(miningLevel);
            miningLevel++;
        }
    }

    private int getXpRequiredForLevel(int currentLevel) {
        return BASE_XP_PER_LEVEL + (currentLevel - 1) * XP_INCREMENT;
    }

    public int getMiningLevel() {
        return miningLevel;
    }

    public int getMiningXp() {
        return miningXp;
    }

    public void setMiningLevel(int level) {
        this.miningLevel = level;
    }

    public void setMiningXp(int xp) {
        this.miningXp = xp;
    }

    public int getTotalXpForNextLevel() {
        return getXpRequiredForLevel(miningLevel);
    }

    public double getEfficiencyBonus() {
        int efficiencyLevels = miningLevel - (miningLevel / 5);
        return efficiencyLevels * 0.01;
    }

    public double getFortuneBonus() {
        int fortuneLevels = miningLevel / 5;
        return fortuneLevels * 0.01;
    }
}
