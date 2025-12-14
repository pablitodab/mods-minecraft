package com.pablo.pablosmod.level;

import com.pablo.pablosmod.PablosMod;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = PablosMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerAgilityEvents {

    // Para medir distancia recorrida por jugador
    private static final java.util.Map<java.util.UUID, BlockPos> LAST_POS = new java.util.HashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = event.player;
        if (player.level().isClientSide) return;

        BlockPos current = player.blockPosition();
        BlockPos last = LAST_POS.get(player.getUUID());
        LAST_POS.put(player.getUUID(), current);

        if (last == null) return;

        double dx = current.getX() - last.getX();
        double dz = current.getZ() - last.getZ();
        double dist2 = dx * dx + dz * dz;
        if (dist2 < 1.0) return; // no se ha movido al menos 1 bloque

        PlayerAgilityData data = PlayerAgilityProvider.getData(player);
        int oldLevel = data.getAgilityLevel();

        // 1 XP por cada bloque aproximado
        int xpGain = (int) Math.round(Math.sqrt(dist2));
        data.addXp(xpGain);

        if (data.getAgilityLevel() > oldLevel) {
            player.displayClientMessage(
                    Component.literal("§6¡Has subido al nivel " + data.getAgilityLevel() + " de agilidad!"),
                    false
            );

            double speed = data.getSpeedBonus();
            double jump = data.getJumpBonus();
            double fall = data.getFallReduction() * 100.0;

            player.displayClientMessage(
                    Component.literal("§eVelocidad: §6+" + String.format("%.1f", speed * 100) +
                            "% §eSalto: §6+" + String.format("%.1f", jump * 100) +
                            "% §eCaída: §6-" + String.format("%.1f", fall) + "% daño"),
                    false
            );
        }

        int xpTotal = data.getTotalXpForNextLevel();
        int xpActual = data.getAgilityXp();
        player.displayClientMessage(
                Component.literal("§eNivel " + data.getAgilityLevel() + " XP: " + xpActual + "/" + xpTotal),
                true
        );

        PlayerAgilityProvider.setData(player, data);
        applyAgilityEffects(player, data);
    }

    // Bonus extra al descubrir nuevo bioma (se dispara cuando cambia de bioma)
    @SubscribeEvent
    public static void onBiomeChange(PlayerEvent.HarvestCheck event) {
        // Este evento no es ideal para biomas, pero como simplificación:
        // podrías más adelante cambiarlo por un evento específico de bioma si lo prefieres.
    }

    @SubscribeEvent
    public static void onFall(LivingFallEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide) return;

        PlayerAgilityData data = PlayerAgilityProvider.getData(player);
        double reduction = data.getFallReduction(); // 0.0–0.30
        if (reduction <= 0) return;

        float oldDamage = event.getDamageMultiplier();
        event.setDamageMultiplier((float) (oldDamage * (1.0 - reduction)));
    }

    private static void applyAgilityEffects(Player player, PlayerAgilityData data) {
        double speedBonus = data.getSpeedBonus();
        double jumpBonus = data.getJumpBonus();

        if (speedBonus > 0) {
            int speedLevel = (int) (speedBonus * 100);
            if (speedLevel > 0) {
                player.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SPEED,
                        40,
                        Math.min(speedLevel - 1, 4),
                        false,
                        false
                ));
            }
        }

        if (jumpBonus > 0) {
            int jumpLevel = (int) (jumpBonus * 100);
            if (jumpLevel > 0) {
                player.addEffect(new MobEffectInstance(
                        MobEffects.JUMP,
                        40,
                        Math.min(jumpLevel - 1, 4),
                        false,
                        false
                ));
            }
        }
    }
}
