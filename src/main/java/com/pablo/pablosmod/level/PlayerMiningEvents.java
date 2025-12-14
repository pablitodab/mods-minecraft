package com.pablo.pablosmod.level;

import com.pablo.pablosmod.PablosMod;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.BlockTags;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = PablosMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerMiningEvents {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();

        if (player.level().isClientSide) {
            return;
        }

        if (!(player.getMainHandItem().getItem() instanceof PickaxeItem)) {
            return;
        }

        if (!isMinableBlock(event.getState().getBlock())) {
            return;
        }

        PlayerMiningData data = PlayerMiningProvider.getData(player);
        int oldLevel = data.getMiningLevel();

        int xpGain = getXpForBlock(event.getState().getBlock());
        data.addXp(xpGain);

        if (data.getMiningLevel() > oldLevel) {
            player.displayClientMessage(
                    net.minecraft.network.chat.Component.literal(
                            "§6§l¡¡ Has subido al nivel " + data.getMiningLevel() + " de minería !!"),
                    false);

            double effBonus = data.getEfficiencyBonus();
            double fortBonus = data.getFortuneBonus();

            player.displayClientMessage(
                    net.minecraft.network.chat.Component.literal(
                            "§e[Eficiencia: " + String.format("%.1f", effBonus * 100) + "% | " +
                                    "Fortuna: " + String.format("%.1f", fortBonus * 100) + "%]"),
                    false);

            if (data.getMiningLevel() == 150) {
                player.getInventory().add(new net.minecraft.world.item.ItemStack(
                        com.pablo.pablosmod.ModItems.PABLOS_PICKAXE.get()
                ));
                player.displayClientMessage(
                        net.minecraft.network.chat.Component.literal("§b¡Has recibido el Pico de Pablo por alcanzar nivel 150!"),
                        false
                );
            }

        }

        int xpTotal = data.getTotalXpForNextLevel();
        int xpActual = data.getMiningXp();
        player.displayClientMessage(
                net.minecraft.network.chat.Component.literal(
                        "§eNivel " + data.getMiningLevel() + " | XP: " + xpActual + " / " + xpTotal),
                true);

        PlayerMiningProvider.setData(player, data);
        applyEfficiencyBonus(player, data);
        applyFortuneBonus(event, data);
    }

    private static void applyEfficiencyBonus(Player player, PlayerMiningData data) {
        double efficiencyBonus = data.getEfficiencyBonus();

        if (efficiencyBonus > 0) {
            int hasteLevel = (int) (efficiencyBonus * 100);
            if (hasteLevel > 0) {
                player.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                        net.minecraft.world.effect.MobEffects.DIG_SPEED,
                        2,
                        Math.min(hasteLevel - 1, 4),
                        false,
                        false
                ));
            }
        }
    }

    private static void applyFortuneBonus(BlockEvent.BreakEvent event, PlayerMiningData data) {
        double fortuneBonus = data.getFortuneBonus();

        if (fortuneBonus > 0) {
            Player player = event.getPlayer();
            if (Math.random() < fortuneBonus) {
                player.displayClientMessage(
                        net.minecraft.network.chat.Component.literal(
                                "§b*** ¡Drop bonus de fortuna! ***"),
                        true);
            }
        }
    }

    private static int getXpForBlock(net.minecraft.world.level.block.Block block) {
        if (!block.defaultBlockState().is(BlockTags.MINEABLE_WITH_PICKAXE)) {
            return 0;
        }

        if (block == Blocks.STONE || block == Blocks.DEEPSLATE ||
                block == Blocks.BASALT || block == Blocks.BLACKSTONE ||
                block == Blocks.ANDESITE || block == Blocks.DIORITE ||
                block == Blocks.GRANITE || block == Blocks.TUFF ||
                block == Blocks.COBBLESTONE || block == Blocks.SANDSTONE) {
            return 5;
        } else if (block == Blocks.NETHERRACK || block == Blocks.ICE ||
                block == Blocks.BLUE_ICE || block == Blocks.FROSTED_ICE ||
                block == Blocks.PACKED_ICE || block == Blocks.AMETHYST_BLOCK) {
            return 1;
        } else if (block == Blocks.COAL_ORE || block == Blocks.DEEPSLATE_COAL_ORE ||
                block == Blocks.END_STONE || block == Blocks.COPPER_ORE ||
                block == Blocks.DEEPSLATE_COPPER_ORE || block == Blocks.NETHER_QUARTZ_ORE ||
                block == Blocks.NETHER_GOLD_ORE) {
            return 10;
        } else if (block == Blocks.IRON_ORE || block == Blocks.DEEPSLATE_IRON_ORE || block == Blocks.OBSIDIAN || block == Blocks.CRYING_OBSIDIAN ) {
            return 15;
        } else if (block == Blocks.GOLD_ORE || block == Blocks.DEEPSLATE_GOLD_ORE) {
            return 20;
        } else if (block == Blocks.DIAMOND_ORE || block == Blocks.DEEPSLATE_DIAMOND_ORE) {
            return 40;
        } else if (block == Blocks.EMERALD_ORE || block == Blocks.DEEPSLATE_EMERALD_ORE) {
            return 50;
        } else if (block == Blocks.LAPIS_ORE || block == Blocks.DEEPSLATE_LAPIS_ORE ||
                block == Blocks.REDSTONE_ORE || block == Blocks.DEEPSLATE_REDSTONE_ORE) {
            return 25;
        }
        return 0;
    }

    private static boolean isMinableBlock(Block block) {
        return block.defaultBlockState().is(BlockTags.MINEABLE_WITH_PICKAXE);
    }
}
