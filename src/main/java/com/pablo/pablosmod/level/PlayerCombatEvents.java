package com.pablo.pablosmod.level;

import com.pablo.pablosmod.PablosMod;
import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Mule;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.breeze.Breeze;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = PablosMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerCombatEvents {

    @SubscribeEvent
    public static void onEntityKill(LivingDeathEvent event) {
        if (!(event.getSource().getEntity() instanceof Player player)) {
            return;
        }

        if (player.level().isClientSide) {
            return;
        }

        if (!(player.getMainHandItem().getItem() instanceof SwordItem
                || player.getMainHandItem().getItem() instanceof AxeItem)) {
            return;
        }

        PlayerCombatData data = PlayerCombatProvider.getData(player);
        int oldLevel = data.getCombatLevel();
        int xpGain = getXpForMob(event.getEntity());

        data.addXp(xpGain);

        if (data.getCombatLevel() > oldLevel) {
            player.displayClientMessage(
                    Component.literal("§6¡Has subido al nivel " + data.getCombatLevel() + " de combate!"),
                    false
            );

            double strBonus = data.getStrengthBonus();
            double lootBonus = data.getLootingBonus();

            player.displayClientMessage(
                    Component.literal("§eFuerza: §6+" + String.format("%.1f", strBonus * 100) +
                            "% §eBotín: §6+" + String.format("%.1f", lootBonus * 100) + "%"),
                    false
            );
        }
        int xpTotal = data.getTotalXpForNextLevel();
        int xpActual = data.getCombatXp();
        player.displayClientMessage(
                Component.literal("§eNivel " + data.getCombatLevel() + " XP: " + xpActual + "/" + xpTotal),
                true
        );

        PlayerCombatProvider.setData(player, data);
        applyStrengthBonus(player, data);
        applyLootingBonus(event, player, data);
    }

    private static void applyStrengthBonus(Player player, PlayerCombatData data) {
        double strengthBonus = data.getStrengthBonus();
        if (strengthBonus > 0) {
            int strengthLevel = (int) (strengthBonus * 100);
            if (strengthLevel > 0) {
                player.addEffect(new MobEffectInstance(
                        MobEffects.DAMAGE_BOOST,
                        40,
                        Math.min(strengthLevel - 1, 4),
                        false,
                        false
                ));
            }
        }
    }

    private static void applyLootingBonus(LivingDeathEvent event, Player player, PlayerCombatData data) {
        double lootingBonus = data.getLootingBonus();
        if (lootingBonus > 0 && Math.random() < lootingBonus) {
            player.displayClientMessage(
                    Component.literal("§b¡Botín extra de combate!"),
                    true
            );
        }
    }

    private static int getXpForMob(LivingEntity entity) {
        if (entity instanceof net.minecraft.world.entity.boss.enderdragon.EnderDragon) return 1000;
        if (entity instanceof net.minecraft.world.entity.boss.wither.WitherBoss) return 800;

        if (entity instanceof Warden) return 120;
        if (entity instanceof Ravager) return 80;
        if (entity instanceof ElderGuardian)  return 60;
        if (entity instanceof WitherSkeleton) return 45;

        if (entity instanceof Evoker) return 40;
        if (entity instanceof Vex) return 35;
        if (entity instanceof Blaze) return 30;
        if (entity instanceof Ghast) return 30;
        if (entity instanceof Guardian) return 25;
        if (entity instanceof EnderMan) return 25;
        if (entity instanceof Vindicator) return 25;
        if (entity instanceof Hoglin) return 25;
        if (entity instanceof Zoglin) return 25;
        if (entity instanceof MagmaCube) return 20;

        if (entity instanceof Husk) return 15;
        if (entity instanceof Drowned) return 15;
        if (entity instanceof Stray) return 15;
        if (entity instanceof Witch) return 20;
        if (entity instanceof Creeper) return 18;
        if (entity instanceof Spider) return 14;
        if (entity instanceof CaveSpider) return 16;
        if (entity instanceof Silverfish) return 14;
        if (entity instanceof Endermite) return 16;
        if (entity instanceof Slime) return 12;
        if (entity instanceof Phantom) return 18;
        if (entity instanceof Shulker) return 30;

        if (entity instanceof Zombie) return 10;
        if (entity instanceof Skeleton) return 10;
        if (entity instanceof ZombieVillager) return 12;
        if (entity instanceof Pillager) return 12;
        if (entity instanceof Strider) return 8;
        if (entity instanceof Piglin) return 12;
        if (entity instanceof ZombifiedPiglin) return 12;
        if (entity instanceof Breeze) return 20;

        if (entity instanceof IronGolem) return 30;
        if (entity instanceof SnowGolem) return 10;
        if (entity instanceof Allay) return 20;

        if (entity instanceof Sniffer) return 20;
        if (entity instanceof Axolotl) return 12;
        if (entity instanceof Horse
                || entity instanceof Donkey
                || entity instanceof Mule
                || entity instanceof SkeletonHorse) return 10;
        if (entity instanceof Dolphin) return 12;
        if (entity instanceof Camel) return 10;
        if (entity instanceof Goat) return 8;
        if (entity instanceof Parrot) return 10;
        if (entity instanceof TropicalFish
                || entity instanceof Pufferfish
                || entity instanceof Salmon
                || entity instanceof Cod) return 6;
        if (entity instanceof Squid
                || entity instanceof GlowSquid) return 7;
        if (entity instanceof MushroomCow) return 12;
        if (entity instanceof Fox) return 9;
        if (entity instanceof Turtle) return 10;
        if (entity instanceof Frog
                || entity instanceof Tadpole) return 6;
        if (entity instanceof Cat
                || entity instanceof Ocelot) return 8;
        if (entity instanceof Rabbit) return 6;
        if (entity instanceof Wolf) return 8;
        if (entity instanceof PolarBear) return 14;
        if (entity instanceof Panda) return 12;

        if (entity instanceof Cow
                || entity instanceof Chicken
                || entity instanceof Pig
                || entity instanceof Sheep)return 5;
        if (entity instanceof Bee)return 6;

        if (entity instanceof Villager)return 10;
        if (entity instanceof WanderingTrader)return 12;
        return 5;
    }

}
