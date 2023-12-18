package com.silvermoon.boxplusplus.client;

import net.minecraftforge.common.MinecraftForge;

import com.silvermoon.boxplusplus.common.CommonProxy;
import com.silvermoon.boxplusplus.common.render.RenderBoxRing;
import com.silvermoon.boxplusplus.common.tileentities.TEBoxRing;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
        ClientRegistry.bindTileEntitySpecialRenderer(TEBoxRing.class, new RenderBoxRing());
        /*
         * ClientRegistry.bindTileEntitySpecialRenderer(
         * (Class<? extends TileEntity>) TileEntitiesLoader.DroneMaintainingCentre.getBaseMetaTileEntity()
         * .getClass(),
         * new RenderDrone());
         */
        MinecraftForge.EVENT_BUS.register(BoxNEIHandler.instance);
    }
}
