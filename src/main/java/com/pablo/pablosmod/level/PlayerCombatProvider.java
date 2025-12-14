package com.pablo.pablosmod.level;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class PlayerCombatProvider {
    private static final String COMBAT_DATA_TAG = "pablosmodcombat";

    public static PlayerCombatData getData(Player player) {
        if (!(player instanceof ServerPlayer)) {
            return new PlayerCombatData();
        }

        CompoundTag tag = player.getPersistentData();
        PlayerCombatData data = new PlayerCombatData();

        if (tag.contains(COMBAT_DATA_TAG)) {
            CompoundTag combatTag = tag.getCompound(COMBAT_DATA_TAG);
            data.setCombatLevel(combatTag.getInt("level"));
            data.setCombatXp(combatTag.getInt("xp"));
        }

        return data;
    }

    public static void setData(Player player, PlayerCombatData data) {
        if (!(player instanceof ServerPlayer)) {
            return;
        }

        CompoundTag tag = player.getPersistentData();
        CompoundTag combatTag = new CompoundTag();
        combatTag.putInt("level", data.getCombatLevel());
        combatTag.putInt("xp", data.getCombatXp());
        tag.put(COMBAT_DATA_TAG, combatTag);
    }
}
