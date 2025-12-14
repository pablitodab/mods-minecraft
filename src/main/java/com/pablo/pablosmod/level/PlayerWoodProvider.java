package com.pablo.pablosmod.level;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class PlayerWoodProvider {

    private static final String WOOD_DATA_TAG = "pablosmodwood";

    public static PlayerWoodData getData(Player player) {
        if (!(player instanceof ServerPlayer)) {
            return new PlayerWoodData();
        }

        CompoundTag tag = player.getPersistentData();
        PlayerWoodData data = new PlayerWoodData();

        if (tag.contains(WOOD_DATA_TAG)) {
            CompoundTag woodTag = tag.getCompound(WOOD_DATA_TAG);
            data.setWoodLevel(woodTag.getInt("level"));
            data.setWoodXp(woodTag.getInt("xp"));
        }

        return data;
    }

    public static void setData(Player player, PlayerWoodData data) {
        if (!(player instanceof ServerPlayer)) {
            return;
        }

        CompoundTag tag = player.getPersistentData();
        CompoundTag woodTag = new CompoundTag();
        woodTag.putInt("level", data.getWoodLevel());
        woodTag.putInt("xp", data.getWoodXp());
        tag.put(WOOD_DATA_TAG, woodTag);
    }
}
