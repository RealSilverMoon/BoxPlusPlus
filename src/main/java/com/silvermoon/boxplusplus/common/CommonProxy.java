package com.silvermoon.boxplusplus.common;

import com.silvermoon.boxplusplus.common.config.Config;
import com.silvermoon.boxplusplus.common.loader.BlockRegister;
import com.silvermoon.boxplusplus.common.loader.ItemRegister;
import com.silvermoon.boxplusplus.common.loader.RecipeLoader;
import com.silvermoon.boxplusplus.common.loader.TileEntitiesLoader;
import com.silvermoon.boxplusplus.network.NetworkLoader;
import com.silvermoon.boxplusplus.util.ResultModuleRequirement;

import cpw.mods.fml.common.event.*;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;

public class CommonProxy {

    // preInit "Run before anything else. Read your config, create blocks, items, etc., and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        Config.synchronizeConfiguration(event.getSuggestedConfigurationFile());
        BlockRegister.register();
        ItemRegister.register();
        NetworkLoader.init();
    }

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {
        TileEntitiesLoader.register();
        CheckRecipeResultRegistry.register(new ResultModuleRequirement(0, false));
    }

    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {}

    public void serverStarted(FMLServerStartedEvent event) {

    }

    public void loadCompeted(FMLLoadCompleteEvent event) {
        new RecipeLoader().run();
    }
}
