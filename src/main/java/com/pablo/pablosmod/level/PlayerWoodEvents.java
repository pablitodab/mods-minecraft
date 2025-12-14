package com.pablo.pablosmod.level;

import com.pablo.pablosmod.PablosMod;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.network.chat.Component;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = PablosMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerWoodEvents {

    @SubscribeEvent
    public static void onLogBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();

        if (player.level().isClientSide) {
            return;
        }

        if (!(player.getMainHandItem().getItem() instanceof AxeItem)) {
            return;
        }

        Block block = event.getState().getBlock();
        if (!block.defaultBlockState().is(BlockTags.LOGS)) {
            return;
        }

        PlayerWoodData data = PlayerWoodProvider.getData(player);
        int oldLevel = data.getWoodLevel();

        int xpGain = getXpForLog(block);
        data.addXp(xpGain);

        if (data.getWoodLevel() > oldLevel) {
            player.displayClientMessage(
                    Component.literal("§6¡¡ Has subido al nivel " + data.getWoodLevel() + " de leñador !!"),
                    false
            );

            double effBonus = data.getEfficiencyBonus();
            double fortBonus = data.getFortuneBonus();

            player.displayClientMessage(
                    Component.literal(
                            "§e[Rapidez tala: " + String.format("%.1f", effBonus * 100) + "% | " +
                                    "Botín madera: " + String.format("%.1f", fortBonus * 100) + "%]"),
                    false
            );
        }

        int xpTotal = data.getTotalXpForNextLevel();
        int xpActual = data.getWoodXp();
        player.displayClientMessage(
                Component.literal("§eNivel " + data.getWoodLevel() + " | XP: " + xpActual + " / " + xpTotal),
                true
        );

        PlayerWoodProvider.setData(player, data);
        applyWoodEfficiencyBonus(player, data);
        applyWoodFortuneBonus(event, data);
    }

    private static void applyWoodEfficiencyBonus(Player player, PlayerWoodData data) {
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

    private static void applyWoodFortuneBonus(BlockEvent.BreakEvent event, PlayerWoodData data) {
        double fortuneBonus = data.getFortuneBonus();
        if (fortuneBonus > 0 && Math.random() < fortuneBonus) {
            Player player = event.getPlayer();
            player.displayClientMessage(
                    Component.literal("§b¡Botín extra de madera!"),
                    true
            );
        }
    }

    private static int getXpForLog(Block block) {
        return 10;
    }
}
