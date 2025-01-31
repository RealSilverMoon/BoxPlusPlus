package com.silvermoon.boxplusplus.common;

import net.minecraftforge.common.MinecraftForge;

import com.silvermoon.boxplusplus.common.config.Config;
import com.silvermoon.boxplusplus.common.loader.BlockRegister;
import com.silvermoon.boxplusplus.common.loader.RecipeLoader;
import com.silvermoon.boxplusplus.common.loader.TileEntitiesLoader;
import com.silvermoon.boxplusplus.event.ServerEvent;
import com.silvermoon.boxplusplus.network.NetworkLoader;
import com.silvermoon.boxplusplus.util.ResultModuleRequirement;

import bartworks.API.SideReference;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.*;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;

public class CommonProxy {

    // preInit "Run before anything else. Read your config, create blocks, items, etc., and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        Config.synchronizeConfiguration(event.getSuggestedConfigurationFile());
        BlockRegister.register();
        NetworkLoader.init();
    }

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {
        TileEntitiesLoader.register();
        CheckRecipeResultRegistry.register(new ResultModuleRequirement(0, false));
        ServerEvent serverEvent = new ServerEvent();
        if (SideReference.Side.Server) {
            MinecraftForge.EVENT_BUS.register(serverEvent);
        }
        FMLCommonHandler.instance()
            .bus()
            .register(serverEvent);
    }

    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {
        new RecipeLoader().run();
    }

    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {}

    public void serverStarted(FMLServerStartedEvent event) {}
}
