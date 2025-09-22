package com.silvermoon.boxplusplus.common.block;

import static com.silvermoon.boxplusplus.boxplusplus.BoxTab;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.silvermoon.boxplusplus.Tags;
import com.silvermoon.boxplusplus.common.tileentities.TEBoxRing;

import cpw.mods.fml.common.registry.GameRegistry;

public class BlockBoxRing extends Block implements ITileEntityProvider {

    public BlockBoxRing(int i) {
        super(Material.iron);
        setBlockName("boxplusplus_BoxRing" + i);
        setCreativeTab(BoxTab);
        setHarvestLevel("pickaxe", 2);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TEBoxRing();
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

    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
        if (!world.isRemote) {
            TileEntity te = world.getTileEntity(x, y, z);
            if (te instanceof TEBoxRing ring) {
                ring.scale -= 0.05f;
                if (ring.scale < 0.05f) ring.scale = 0.05f;

                ring.markDirty();
                world.markBlockForUpdate(x, y, z);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX,
        float hitY, float hitZ) {

        if (!world.isRemote) {
            TileEntity te = world.getTileEntity(x, y, z);
            if (te instanceof TEBoxRing ring) {
                if (player.isSneaking()) {
                    ring.scale += 0.05f;
                    if (ring.scale > 3.0f) ring.scale = 3.0f;
                } else {
                    ring.renderStatus = !ring.renderStatus;
                }

                ring.markDirty();
                world.markBlockForUpdate(x, y, z);
            }
        }
        return true;
    }
}
