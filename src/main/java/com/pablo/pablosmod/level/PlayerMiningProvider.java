package com.pablo.pablosmod.level;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class PlayerMiningProvider {

    private static final String MINING_DATA_TAG = "pablos_mod_mining";

    public static PlayerMiningData getData(Player player) {
        if (!(player instanceof ServerPlayer)) {
            return new PlayerMiningData();
        }

        CompoundTag tag = player.getPersistentData();
        PlayerMiningData data = new PlayerMiningData();

        if (tag.contains(MINING_DATA_TAG)) {
            CompoundTag miningTag = tag.getCompound(MINING_DATA_TAG);
            data.setMiningLevel(miningTag.getInt("level"));
            data.setMiningXp(miningTag.getInt("xp"));
        }

        return data;
    }

    public static void setData(Player player, PlayerMiningData data) {
        if (!(player instanceof ServerPlayer)) {
            return;
        }

        CompoundTag tag = player.getPersistentData();
        CompoundTag miningTag = new CompoundTag();

        miningTag.putInt("level", data.getMiningLevel());
        miningTag.putInt("xp", data.getMiningXp());

        tag.put(MINING_DATA_TAG, miningTag);
    }
}
