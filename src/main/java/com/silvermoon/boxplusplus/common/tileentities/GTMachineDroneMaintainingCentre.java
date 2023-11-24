package com.silvermoon.boxplusplus.common.tileentities;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static com.silvermoon.boxplusplus.common.loader.ItemRegister.drone;
import static com.silvermoon.boxplusplus.util.Util.*;
import static goodgenerator.util.DescTextLocalization.BLUE_PRINT_INFO;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.dreammaster.gthandler.casings.GT_Container_CasingsNH;
import com.github.bartimaeusnek.bartworks.API.BorosilicateGlass;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.util.Vec3Impl;
import com.silvermoon.boxplusplus.util.Util;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;

public class GTMachineDroneMaintainingCentre extends
    GT_MetaTileEntity_ExtendedPowerMultiBlockBase<GTMachineDroneMaintainingCentre> implements ISurvivalConstructable {

    private static final IIconContainer Active = new Textures.BlockIcons.CustomIcon("iconsets/droneCentre");
    private static final IIconContainer Inactive = new Textures.BlockIcons.CustomIcon("iconsets/droneCentre");
    public double rotation = 0;
    public Vec3Impl vec3;
    public int range = 32;
    public int droneLevel = 0;
    private final Random random = new Random();
    // spotless off
    private static final IStructureDefinition<GTMachineDroneMaintainingCentre> STRUCTURE_DEFINITION = StructureDefinition
        .<GTMachineDroneMaintainingCentre>builder()
        .addShape(
            "main",
            transpose(
                new String[][] { { "     ", "     ", "     ", "     ", "CCCCC", "CCCCC", "CCCCC", "CCCCC", "CCCCC" },
                    { "CE~EC", "C   C", "C   C", "C   C", "CAAAC", "CCCCC", "CAAAC", "C   C", "CCCCC" },
                    { "CEEEC", "CBBBC", "CBDBC", "CBBBC", "CCCCC", "CCCCC", "CCCCC", "CCCCC", "CCCCC" },
                    { "C   C", "     ", "     ", "     ", "     ", "     ", "     ", "     ", "C   C" },
                    { "C   C", "     ", "     ", "     ", "     ", "     ", "     ", "     ", "C   C" },
                    { "C   C", "     ", "     ", "     ", "     ", "     ", "     ", "     ", "C   C" } }))
        .addElement(
            'E',
            buildHatchAdder(GTMachineDroneMaintainingCentre.class).atLeast(InputBus)
                .casingIndex(59)
                .dot(1)
                .buildAndChain(ofBlock(GregTech_API.sBlockCasings4, 2)))
        .addElement('C', ofBlock(GregTech_API.sBlockCasings4, 2))
        .addElement('A', BorosilicateGlass.ofBoroGlass((byte) 4))
        .addElement('B', ofBlock(GregTech_API.sBlockCasings1, 11))
        .addElement('D', lazy(x -> ofBlock(GT_Container_CasingsNH.sBlockCasingsNH, 5)))
        .build();

    // spotless on
    public GTMachineDroneMaintainingCentre(String name) {
        super(name);
    }

    public GTMachineDroneMaintainingCentre(int ID, String Name, String NameRegional) {
        super(ID, Name, NameRegional);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GTMachineDroneMaintainingCentre(super.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { casingTexturePages[8][67], TextureFactory.builder()
                .addIcon(Active)
                .extFacing()
                .build() };
            return new ITexture[] { casingTexturePages[8][67], TextureFactory.builder()
                .addIcon(Inactive)
                .extFacing()
                .build() };
        }
        return new ITexture[] { casingTexturePages[8][67] };
    }

    @Override
    public IStructureDefinition<GTMachineDroneMaintainingCentre> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(i18n("tile.boxplusplus.DMCinfo.01"))
            .addInfo(i18n("tile.boxplusplus.DMCinfo.02"))
            .addInfo(i18n("tile.boxplusplus.DMCinfo.03"))
            .addInfo(i18n("tile.boxplusplus.DMCinfo.04"))
            .addInfo(i18n("tile.boxplusplus.DMCinfo.05"))
            .addInfo(i18n("tile.boxplusplus.DMCinfo.06"))
            .addInfo(i18n("tile.boxplusplus.DMCinfo.07"))
            .addInfo(i18n("tile.boxplusplus.DMCinfo.08"))
            .addInfo(i18n("tile.boxplusplus.DMCinfo.09"))
            .addInfo(BLUE_PRINT_INFO)
            .addSeparator()
            .beginStructureBlock(3, 3, 3, false)
            .addStructureInfo(i18n("tile.boxplusplus.DMCinfo.10"))
            .addSeparator()
            .toolTipFinisher("BoxPlusPlus");
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece("main", stackSize, hintsOnly, 2, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stack, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece("main", stack, 2, 1, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece("main", 2, 1, 0);
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return true;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        disableMaintenance = true;
        rotation = (rotation + 50) % 360d;
        if (aTick % 200 == 0 && aBaseMetaTileEntity.isServerSide()
            && (droneLevel == 1 || droneLevel == 2)
            && random.nextInt(360 * (3 - droneLevel)) == 0) {
            droneLevel = 0;
            if (!tryConsumeDrone()) criticalStopMachine();
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (getBaseMetaTileEntity().isActive())
            aPlayer.addChatMessage(new ChatComponentText("Can't get drone while running!"));
        if (droneLevel == 0) aPlayer.addChatMessage(new ChatComponentText("No drone inside centre."));
        if (aPlayer.inventory.addItemStackToInventory(new ItemStack(drone, 1, droneLevel - 1))) {
            droneLevel = 0;
            return;
        }
        aPlayer.addChatMessage(new ChatComponentText("No enough slot!"));
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        droneLevel = aNBT.getInteger("drone");
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("drone", droneLevel);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        if (droneLevel != 0) tag.setInteger("DroneLevel: ", droneLevel);
    }

    @Override
    @NotNull
    public CheckRecipeResult checkProcessing() {
        if (droneLevel == 0) {
            if (!tryConsumeDrone()) return SimpleCheckRecipeResult.ofFailure("noDrone");
        }
        mMaxProgresstime = 200 * droneLevel;
        return SimpleCheckRecipeResult.ofSuccess("maintaining");
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        if (aBaseMetaTileEntity.isServerSide()) {
            vec3 = new Vec3Impl(
                getBaseMetaTileEntity().getXCoord(),
                getBaseMetaTileEntity().getYCoord(),
                getBaseMetaTileEntity().getZCoord());
            Util.droneMap.put(getBaseMetaTileEntity().getWorld().provider.dimensionId, this);
        }
    }

    @Override
    public void onRemoval() {
        droneMap.remove(getBaseMetaTileEntity().getWorld().provider.dimensionId, this);
    }

    public boolean tryConsumeDrone() {
        List<ItemStack> inputs = getStoredInputs();
        if (inputs.isEmpty()) return false;
        for (ItemStack item : inputs) {
            if (item != null && item.getItem() == (drone)) {
                this.droneLevel = item.getItemDamage() + 1;
                item.stackSize--;
                break;
            }
            return false;
        }
        return true;
    }
}
