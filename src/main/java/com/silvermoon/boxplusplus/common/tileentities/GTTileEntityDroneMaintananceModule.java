package com.silvermoon.boxplusplus.common.tileentities;

import static com.silvermoon.boxplusplus.util.Util.i18n;

import java.util.*;
import java.util.stream.Collectors;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.util.Vec3Impl;
import com.silvermoon.boxplusplus.util.Util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IMachineBlockUpdateable;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Maintenance;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.render.TextureFactory;

public class GTTileEntityDroneMaintananceModule extends GT_MetaTileEntity_Hatch_Maintenance {

    public Vec3Impl vec3;
    public GT_MetaTileEntity_MultiBlockBase mainframe;
    private static final IIconContainer moduleActive = new Textures.BlockIcons.CustomIcon("iconsets/moduleActive");
    private static final IIconContainer moduleInactive = new Textures.BlockIcons.CustomIcon("iconsets/moduleInactive");

    public GTTileEntityDroneMaintananceModule(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public GTTileEntityDroneMaintananceModule(String aName, int aTier, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures, false);
    }

    @Override
    public String[] getDescription() {
        return new String[] { i18n("内置强力导航信标!") };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        if (mainframe != null && mainframe.getBaseMetaTileEntity()
            .isActive()) return new ITexture[] { aBaseTexture, TextureFactory.of(moduleActive) };
        return new ITexture[] { aBaseTexture, TextureFactory.of(moduleInactive) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(moduleInactive) };
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GTTileEntityDroneMaintananceModule(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        vec3 = new Vec3Impl(
            getBaseMetaTileEntity().getXCoord(),
            getBaseMetaTileEntity().getYCoord(),
            getBaseMetaTileEntity().getZCoord());
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        // save the coord of the mainframe, in case we have to search for it everytime.
        if (hasConnection()) {
            NBTTagCompound coord = new NBTTagCompound();
            coord.setInteger(
                "x",
                mainframe.getBaseMetaTileEntity()
                    .getXCoord());
            coord.setInteger(
                "y",
                mainframe.getBaseMetaTileEntity()
                    .getYCoord());
            coord.setInteger(
                "z",
                mainframe.getBaseMetaTileEntity()
                    .getZCoord());
            aNBT.setTag("mMainframeCoord", coord);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (!hasConnection() && aNBT.hasKey("mMainframeCoord")) {
            NBTTagCompound coord = aNBT.getCompoundTag("mMainframeCoord");
            if (getBaseMetaTileEntity().getWorld()
                .getTileEntity(
                    coord.getInteger("x"),
                    coord.getInteger("y"),
                    coord.getInteger("z")) instanceof IGregTechTileEntity te
                && te.getMetaTileEntity() instanceof GT_MetaTileEntity_MultiBlockBase mte) {
                this.mainframe = mte;
            }
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (Util.droneMap.containsKey(aBaseMetaTileEntity.getWorld().provider.dimensionId)) {
                List<GTMachineDroneMaintainingCentre> target = Util.droneMap.get(aBaseMetaTileEntity.getWorld().provider.dimensionId)
                    .stream()
                    .collect(Collectors.toList());
                for (GTMachineDroneMaintainingCentre DMC : target) {
                    if (DMC.vec3.withinDistance(this.vec3, DMC.range) && DMC.mMaxProgresstime > 0) {
                        this.mWrench = this.mScrewdriver = this.mSoftHammer = this.mHardHammer = this.mCrowbar = this.mSolderingTool = true;
                        return;
                    }
                }
            }
            this.mWrench = this.mScrewdriver = this.mSoftHammer = this.mHardHammer = this.mCrowbar = this.mSolderingTool = false;
            if (aTick % 20 == 0) {
                if (!hasConnection()) mainframe = tryFindGTMultiBlock(this);
                if (mainframe != null) setRandomFault(mainframe);
            }
        }
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
        float aX, float aY, float aZ) {
        return false;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    public boolean hasConnection() {
        return mainframe != null && mainframe.getBaseMetaTileEntity() != null && !mainframe.getBaseMetaTileEntity()
            .isDead();
    }

    private void setRandomFault(GT_MetaTileEntity_MultiBlockBase mte) {
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            switch (random.nextInt(6)) {
                case 0 -> mte.mWrench = false;
                case 1 -> mte.mScrewdriver = false;
                case 2 -> mte.mSoftHammer = false;
                case 3 -> mte.mCrowbar = false;
                case 4 -> mte.mSolderingTool = false;
                case 5 -> mte.mHardHammer = false;
            }
        }
    }

    // Use maintanance hatch to find mainBlock. Mainly from GT_API
    public GT_MetaTileEntity_MultiBlockBase tryFindGTMultiBlock(GTTileEntityDroneMaintananceModule maintain) {
        Queue<ChunkCoordinates> tQueue = new LinkedList<>();
        Set<ChunkCoordinates> visited = new HashSet<>(80);
        tQueue.add(maintain.getBaseMetaTileEntity()
            .getCoords());
        World world = maintain.getBaseMetaTileEntity()
            .getWorld();
        while (!tQueue.isEmpty()) {
            final ChunkCoordinates aCoords = tQueue.poll();
            final TileEntity tTileEntity;
            final boolean isMachineBlock;
            tTileEntity = world.getTileEntity(aCoords.posX, aCoords.posY, aCoords.posZ);
            isMachineBlock = GregTech_API.isMachineBlock(
                world.getBlock(aCoords.posX, aCoords.posY, aCoords.posZ),
                world.getBlockMetadata(aCoords.posX, aCoords.posY, aCoords.posZ));

            // See if the block itself is MultiBlock, also the one we need.
            if (tTileEntity instanceof IGregTechTileEntity te
                && te.getMetaTileEntity() instanceof GT_MetaTileEntity_MultiBlockBase mte)
                if (mte.mMaintenanceHatches.contains(maintain)) return mte;

            // Now see if we should add the nearby blocks to the queue:
            // 1) If we've visited less than 5 blocks, then yes
            // 2) If the tile says we should recursively updated (pipes don't, machine blocks do)
            // 3) If the block at the coordinates is marked as a machine block
            if (visited.size() < 5 || (tTileEntity instanceof IMachineBlockUpdateable
                && ((IMachineBlockUpdateable) tTileEntity).isMachineBlockUpdateRecursive()) || isMachineBlock) {
                ChunkCoordinates tCoords;

                if (visited.add(tCoords = new ChunkCoordinates(aCoords.posX + 1, aCoords.posY, aCoords.posZ)))
                    tQueue.add(tCoords);
                if (visited.add(tCoords = new ChunkCoordinates(aCoords.posX - 1, aCoords.posY, aCoords.posZ)))
                    tQueue.add(tCoords);
                if (visited.add(tCoords = new ChunkCoordinates(aCoords.posX, aCoords.posY + 1, aCoords.posZ)))
                    tQueue.add(tCoords);
                if (visited.add(tCoords = new ChunkCoordinates(aCoords.posX, aCoords.posY - 1, aCoords.posZ)))
                    tQueue.add(tCoords);
                if (visited.add(tCoords = new ChunkCoordinates(aCoords.posX, aCoords.posY, aCoords.posZ + 1)))
                    tQueue.add(tCoords);
                if (visited.add(tCoords = new ChunkCoordinates(aCoords.posX, aCoords.posY, aCoords.posZ - 1)))
                    tQueue.add(tCoords);
            }
        }
        return null;
    }
}
