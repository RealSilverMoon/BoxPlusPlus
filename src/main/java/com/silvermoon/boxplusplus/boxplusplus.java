package com.silvermoon.boxplusplus;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.silvermoon.boxplusplus.common.CommonProxy;
import com.silvermoon.boxplusplus.common.loader.BlockRegister;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;

@Mod(
    modid = Tags.MODID,
    version = Tags.VERSION,
    name = Tags.MODNAME,
    dependencies = "required-after:IC2;" + "required-after:structurelib;"
        + "required-after:modularui;"
        + "after:GalacticraftCore;"
        + "required-after:bartworks;"
        + "after:miscutils;"
        + "after:dreamcraft;"
        + "after:GalacticraftMars;"
        + "after:GalacticraftPlanets",
    acceptedMinecraftVersions = "[1.7.10]")
public class boxplusplus {

    public static final Logger LOG = LogManager.getLogger(Tags.MODID);

    @SidedProxy(
        clientSide = "com.silvermoon.boxplusplus.client.ClientProxy",
        serverSide = "com.silvermoon.boxplusplus.common.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance(Tags.MODID)
    public static boxplusplus instance;

    @Mod.EventHandler
    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }

    @Mod.EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        proxy.serverStarted(event);
    }

    public static final CreativeTabs BoxTab = new CreativeTabs("BoxPlusPlus") {

        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(BlockRegister.BoxRing2);
        }
    };
}
