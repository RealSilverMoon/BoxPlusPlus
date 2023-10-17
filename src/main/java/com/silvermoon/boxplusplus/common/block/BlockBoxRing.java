package com.silvermoon.boxplusplus.common.block;

import com.silvermoon.boxplusplus.Tags;
import com.silvermoon.boxplusplus.common.tileentities.TeBoxRing;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import static com.silvermoon.boxplusplus.boxplusplus.BoxTab;

public class BlockBoxRing extends Block implements ITileEntityProvider {

    public BlockBoxRing(int i) {
        super(Material.iron);
        setBlockName("boxplusplus_BoxRing" + i);
        setCreativeTab(BoxTab);
        setHarvestLevel("pickaxe", 2);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TeBoxRing();
    }

    public void registerBlock() {
        super.setHardness(5);
        super.setCreativeTab(BoxTab);
        GameRegistry.registerBlock(this, getUnlocalizedName());
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public Block setBlockName(String name) {
        super.setBlockName(name);
        this.setBlockTextureName(Tags.MODID + ":" + name);
        return this;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }
}
