package com.pablo.pablosmod;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(PablosMod.MODID)
public class PablosMod {

    public static final String MODID = "pablos_mod";

    public PablosMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Registrar ítems
        ModItems.register(modEventBus);
    }
}
