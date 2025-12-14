package com.pablo.pablosmod.level;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class PlayerAgilityProvider {

    private static final String AGILITY_DATA_TAG = "pablosmodagility";

    public static PlayerAgilityData getData(Player player) {
        if (!(player instanceof ServerPlayer)) {
            return new PlayerAgilityData();
        }

        CompoundTag tag = player.getPersistentData();
        PlayerAgilityData data = new PlayerAgilityData();

        if (tag.contains(AGILITY_DATA_TAG)) {
            CompoundTag agilityTag = tag.getCompound(AGILITY_DATA_TAG);
            data.setAgilityLevel(agilityTag.getInt("level"));
            data.setAgilityXp(agilityTag.getInt("xp"));
        }

        return data;
    }

    public static void setData(Player player, PlayerAgilityData data) {
        if (!(player instanceof ServerPlayer)) {
            return;
        }

        CompoundTag tag = player.getPersistentData();
        CompoundTag agilityTag = new CompoundTag();
        agilityTag.putInt("level", data.getAgilityLevel());
        agilityTag.putInt("xp", data.getAgilityXp());
        tag.put(AGILITY_DATA_TAG, agilityTag);
    }
}
