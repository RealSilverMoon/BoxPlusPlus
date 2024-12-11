package com.silvermoon.boxplusplus.common.loader;

import net.minecraft.block.material.Material;

import com.silvermoon.boxplusplus.common.block.BlockBoxModuleCore;
import com.silvermoon.boxplusplus.common.block.BlockBoxRing;
import com.silvermoon.boxplusplus.common.block.BlockMachineBase;

public class BlockRegister {

    public static BlockMachineBase SpaceExtend = new BlockMachineBase("boxplusplus_SpaceExtend", Material.iron, 0)
        .setHarvest("wrench", 3);
    public static BlockMachineBase SpaceConstraint = new BlockMachineBase(
        "boxplusplus_SpaceConstraint",
        Material.iron,
        2).setHarvest("wrench", 3);
    public static BlockMachineBase SpaceCompress = new BlockMachineBase("boxplusplus_SpaceCompress", Material.iron, 1)
        .setHarvest("wrench", 3);
    public static BlockMachineBase SpaceWall = new BlockMachineBase("boxplusplus_SpaceWall", Material.iron, 3)
        .setHarvest("wrench", 3);
    public static BlockBoxModuleCore BoxModule = new BlockBoxModuleCore("boxplusplus_boxmodule", Material.iron, false)
        .setHarvest("wrench", 5);
    public static BlockBoxModuleCore BoxModuleUpgrad = new BlockBoxModuleCore(
        "boxplusplus_boxmoduleplus",
        Material.iron,
        true).setHarvest("wrench", 5);
    public static BlockBoxRing BoxRing = new BlockBoxRing(1);
    public static BlockBoxRing BoxRing2 = new BlockBoxRing(2);
    public static BlockBoxRing BoxRing3 = new BlockBoxRing(3);

    public static void register() {
        SpaceExtend.registerBlock();
        SpaceConstraint.registerBlock();
        SpaceCompress.registerBlock();
        SpaceWall.registerBlock();
        BoxRing.registerBlock();
        BoxRing2.registerBlock();
        BoxRing3.registerBlock();
        BoxModule.registerBlock();
        BoxModuleUpgrad.registerBlock();
    }
}
