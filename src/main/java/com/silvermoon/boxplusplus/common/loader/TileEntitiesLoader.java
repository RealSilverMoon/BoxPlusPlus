package com.silvermoon.boxplusplus.common.loader;

import static com.silvermoon.boxplusplus.util.Util.i18n;

import com.silvermoon.boxplusplus.common.tileentities.GTMachineBox;
import com.silvermoon.boxplusplus.common.tileentities.GTMachineDroneMaintainingCentre;
import com.silvermoon.boxplusplus.common.tileentities.GTTileEntityDroneMaintananceModule;
import com.silvermoon.boxplusplus.common.tileentities.TEBoxRing;

import cpw.mods.fml.common.registry.GameRegistry;

public class TileEntitiesLoader {

    public static GTMachineBox Box;
    public static GTMachineDroneMaintainingCentre DroneMaintainingCentre;
    public static GTTileEntityDroneMaintananceModule DroneMaintananceModule;

    public static void register() {
        Box = new GTMachineBox(17001, "multimachine_Box", i18n("tile.boxplusplus.box"));
        DroneMaintainingCentre = new GTMachineDroneMaintainingCentre(18001,
            "multimachine_DroneMaintainingCentre",
            i18n("tile.boxplusplus.DroneMaintainingCentre"));
        DroneMaintananceModule = new GTTileEntityDroneMaintananceModule(18002,
            "DroneMaintananceModule",
            "无人机维护中心下行模块",
            5);
        GameRegistry.registerTileEntity(TEBoxRing.class, "BoxRing");
    }
}
