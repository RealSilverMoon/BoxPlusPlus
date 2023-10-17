package com.silvermoon.boxplusplus.common.block;

import com.silvermoon.boxplusplus.Tags;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import static com.silvermoon.boxplusplus.boxplusplus.BoxTab;

public class BlockMachineBase extends Block {

    private IIcon BoxIcon;
    private final int value;
    static {
        GT_Utility.addTexturePage((byte) 114);
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
        GregTech_API.registerMachineBlock(this, -1);
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
