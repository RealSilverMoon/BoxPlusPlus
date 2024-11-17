package com.silvermoon.boxplusplus.common.block;

import static com.silvermoon.boxplusplus.boxplusplus.BoxTab;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.silvermoon.boxplusplus.Tags;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;

public class BlockMachineBase extends Block {

    private IIcon BoxIcon;
    private final int value;
    static {
        GTUtility.addTexturePage((byte) 114);
    }

    public BlockMachineBase(String name, Material material, int value) {
        super(material);
        setBlockName(name);
        this.value = value;
        Textures.BlockIcons.setCasingTexture((byte) 114, (byte) value, TextureFactory.of(this));
    }

    public void registerBlock() {
        super.setHardness(5);
        super.setCreativeTab(BoxTab);
        GameRegistry.registerBlock(this, getUnlocalizedName());
        GregTechAPI.registerMachineBlock(this, -1);
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        BoxIcon = iconRegister.registerIcon(Tags.MODID + ":boxCasing" + value);
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return BoxIcon;
    }

    @Override
    public Block setBlockName(String name) {
        super.setBlockName(name);
        this.setBlockTextureName(Tags.MODID + ":" + name);
        return this;
    }

    public BlockMachineBase setHarvest(String tool, int hardness) {
        super.setHarvestLevel(tool, hardness);
        return this;
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public void onBlockAdded(World aWorld, int aX, int aY, int aZ) {
        if (GregTechAPI.isMachineBlock(this, aWorld.getBlockMetadata(aX, aY, aZ))) {
            GregTechAPI.causeMachineUpdate(aWorld, aX, aY, aZ);
        }
    }

    @Override
    public void breakBlock(World aWorld, int aX, int aY, int aZ, Block aBlock, int aMetaData) {
        if (GregTechAPI.isMachineBlock(this, aWorld.getBlockMetadata(aX, aY, aZ))) {
            GregTechAPI.causeMachineUpdate(aWorld, aX, aY, aZ);
        }
    }
}
