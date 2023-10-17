package com.silvermoon.boxplusplus.common.loader;

import com.silvermoon.boxplusplus.common.tileentities.GTMachineBox;
import com.silvermoon.boxplusplus.common.tileentities.TeBoxRing;
import cpw.mods.fml.common.registry.GameRegistry;

import static com.silvermoon.boxplusplus.util.Util.i18n;

public class TileEntitiesLoader {

    public static GTMachineBox Box;

    public static void register() {
        Box = new GTMachineBox(17001, "multimachine_Box", i18n("tile.boxplusplus.box"));
        GameRegistry.registerTileEntity(TeBoxRing.class, "BoxRing");
    }
}
