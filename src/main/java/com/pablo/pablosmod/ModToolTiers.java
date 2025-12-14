package com.pablo.pablosmod;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public enum ModToolTiers implements Tier {
    PABLOS_TIER;
    public int getUses() {
        return 1000000;
    }
    public float getSpeed() {
        return 40.0F;
    }
    public float getAttackDamageBonus() {
        return 6.0F;
    }
    public int getEnchantmentValue() {
        return 15;
    }
    public @NotNull Ingredient getRepairIngredient() {
        return Ingredient.EMPTY;
    }
    public @NotNull TagKey<Block> getIncorrectBlocksForDrops() {
        return BlockTags.INCORRECT_FOR_DIAMOND_TOOL;
    }
}
