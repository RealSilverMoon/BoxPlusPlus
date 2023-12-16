package com.silvermoon.boxplusplus.common.block;

import static com.silvermoon.boxplusplus.boxplusplus.BoxTab;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.silvermoon.boxplusplus.Tags;
import com.silvermoon.boxplusplus.common.items.IB_BoxModule;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;

public class BlockBoxModuleCore extends Block {

    private final IIcon[] ModuleIcon = new IIcon[15];
    public final boolean isUpdate;

    public BlockBoxModuleCore(String name, Material material, Boolean isUpdate) {
        super(material);
        this.isUpdate = isUpdate;
        setBlockName(name);
        GregTech_API.registerMachineBlock(this, -1);
    }

    public void registerBlock() {
        super.setHardness(5);
        super.setCreativeTab(BoxTab);
        if (isUpdate) {
            GameRegistry.registerBlock(this, IB_BoxModule.class, "boxplusplus_boxmodule");
        } else GameRegistry.registerBlock(this, IB_BoxModule.class, "boxplusplus_boxmoduleplus");
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        for (int i = 0; i < 15; i++) {
            ModuleIcon[i] = iconRegister.registerIcon(Tags.MODID + (!isUpdate
                ? ":modules/BoxModule"
                : ":modules/BoxModulePlus") + i);
        }
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return ModuleIcon[meta];
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    public BlockBoxModuleCore setHarvest(String tool, int hardness) {
        super.setHarvestLevel(tool, hardness);
        return this;
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tabs, List list) {
        for (int i = 0; i < 15; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public void onBlockAdded(World aWorld, int aX, int aY, int aZ) {
        if (GregTech_API.isMachineBlock(this, aWorld.getBlockMetadata(aX, aY, aZ))) {
            GregTech_API.causeMachineUpdate(aWorld, aX, aY, aZ);
        }
    }

    @Override
    public void breakBlock(World aWorld, int aX, int aY, int aZ, Block aBlock, int aMetaData) {
        if (GregTech_API.isMachineBlock(this, aWorld.getBlockMetadata(aX, aY, aZ))) {
            GregTech_API.causeMachineUpdate(aWorld, aX, aY, aZ);
        }
    }
}
