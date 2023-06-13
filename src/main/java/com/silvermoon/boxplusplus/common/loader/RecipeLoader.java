package com.silvermoon.boxplusplus.common.loader;

import com.github.technus.tectech.recipe.TT_recipeAdder;
import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import static gregtech.api.enums.Mods.*;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sAssemblerRecipes;
import static gregtech.api.util.GT_RecipeConstants.*;

public class RecipeLoader implements Runnable {
    //todo: Add recipes
    public void run() {
        addBoxRecipe();
        addMachineBlockRecipe();
        //addModuleRecipe();
        addRingRecipe();
    }

    public void addBoxRecipe() {
        GT_Values.RA.stdBuilder().metadata(RESEARCH_ITEM, ItemList.Machine_Multi_Assemblyline.get(1))
            .metadata(RESEARCH_TIME, 24000)
            .itemOutputs(TileEntitiesLoader.Box.getStackForm(1))
            .noFluidOutputs()
            .itemInputs(
                new ItemStack(BlockRegister.BoxRing, 1, 0),
                GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 4, 59),
                GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 8, 58),
                GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 16, 57),
                GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 32, 38),
                GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 64, 37),
                new ItemStack(BlockRegister.SpaceExtend, 64, 0),
                new ItemStack(BlockRegister.SpaceExtend, 64, 0),
                new ItemStack(BlockRegister.SpaceExtend, 64, 0),
                new ItemStack(BlockRegister.SpaceExtend, 64, 0),
                ItemList.Field_Generator_IV.get(4),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 4, 15470),
                ItemList.Tool_DataOrb.get(16),
                ItemList.Casing_Pipe_Titanium.get(64)
            )
            .fluidInputs(
                FluidRegistry.getFluidStack("molten.titanium", 16416),
                FluidRegistry.getFluidStack("lubricant", 16000),
                FluidRegistry.getFluidStack("ic2uumater", 4000)
            )
            .eut(TierEU.RECIPE_LuV)
            .duration(8000)
            .addTo(AssemblyLine);
    }

    public void addMachineBlockRecipe() {
        GT_Values.RA.stdBuilder().itemOutputs(new ItemStack(BlockRegister.SpaceExtend, 4, 0))
            .noFluidOutputs()
            .itemInputs(
                ItemList.Casing_StableTitanium.get(32),
                GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1, 33),
                GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.IrradiantReinforcedTitaniumPlate", 1))
            .noFluidInputs()
            .eut(TierEU.RECIPE_LuV)
            .duration(400)
            .addTo(sAssemblerRecipes);
        GT_Values.RA.stdBuilder().metadata(RESEARCH_ITEM, new ItemStack(BlockRegister.SpaceCompress))
            .metadata(RESEARCH_TIME, 24000)
            .itemOutputs(new ItemStack(BlockRegister.SpaceCompress, 2, 0))
            .noFluidOutputs()
            .itemInputs(
                new ItemStack(BlockRegister.SpaceExtend, 4),
                ItemList.Electric_Piston_LuV.get(4),
                GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 2, 34),
                GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "tile.TitaniumPlatedReinforcedStone", 64))
            .noFluidInputs()
            .eut(TierEU.RECIPE_UV)
            .duration(1600)
            .addTo(AssemblyLine);
        GT_Values.RA.stdBuilder().metadata(RESEARCH_ITEM, new ItemStack(BlockRegister.SpaceCompress))
            .metadata(RESEARCH_TIME, 48000)
            .itemOutputs(new ItemStack(BlockRegister.SpaceConstraint, 1, 0))
            .noFluidOutputs()
            .itemInputs(
                new ItemStack(BlockRegister.SpaceExtend, 16),
                GT_OreDictUnificator.get("plateDoubleAdvancedNitinol", 16))
            .fluidInputs(FluidRegistry.getFluidStack("supercoolent", 20000))
            .eut(TierEU.RECIPE_UV)
            .duration(1600)
            .addTo(AssemblyLine);
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(BlockRegister.SpaceConstraint),
            480000,
            128,
            2000000,
            8,
            new ItemStack[]{
                new ItemStack(BlockRegister.SpaceExtend, 64),
                GT_OreDictUnificator.get("blockAstralTitanium", 4),
                GT_OreDictUnificator.get("naniteNeutronium", 4),
            },
            new FluidStack[]{
                FluidRegistry.getFluidStack("molten.astraltitanium", 14400),
                FluidRegistry.getFluidStack("molten.chromaticglass", 14400)},
            new ItemStack(BlockRegister.SpaceWall, 1),
            3600,
            (int) TierEU.RECIPE_UHV);
    }

    public void addRingRecipe() {
        GT_Values.RA.stdBuilder().itemOutputs(new ItemStack(BlockRegister.BoxRing, 1, 0))
            .noFluidOutputs()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Titanium, 64),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Titanium, 64),
                GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Titanium, 64),
                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Titanium, 64))
            .fluidInputs(FluidRegistry.getFluidStack("molten.tanmolyium beta-c", 5184))
            .eut(TierEU.RECIPE_LuV)
            .duration(1200)
            .addTo(sAssemblerRecipes);
        GT_Values.RA.stdBuilder().metadata(RESEARCH_ITEM, new ItemStack(BlockRegister.BoxRing))
            .metadata(RESEARCH_TIME, 48000)
            .itemOutputs(new ItemStack(BlockRegister.BoxRing2, 1, 0))
            .noFluidOutputs()
            .itemInputs(
                GT_OreDictUnificator.get("ringLaurenium", 64),
                GT_OreDictUnificator.get("ringLaurenium", 64),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "particleIon", 32, 21),
                ItemList.ZPM_Coil.get(48))
            .fluidInputs(FluidRegistry.getFluidStack("plasma.titanium", 16000))
            .eut(TierEU.RECIPE_UV)
            .duration(60000)
            .addTo(AssemblyLine);
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(BlockRegister.BoxRing2),
            4800000,
            1228,
            8000000,
            16,
            new ItemStack[]{
                GT_OreDictUnificator.get("ringAstralTitanium", 64),
                GT_OreDictUnificator.get("ringAstralTitanium", 64),
                GT_ModHandler.getModItem(UniversalSingularities.ID, "universal.general.singularity", 4, 12),
                GT_OreDictUnificator.get("circuitBio", 4),
                GT_OreDictUnificator.get("batteryUV", 4),
                ItemList.Field_Generator_UEV.get(4)},
            new FluidStack[]{FluidRegistry.getFluidStack("molten.radoxpoly", 64000),
                FluidRegistry.getFluidStack("molten.chromaticglass", 14400)},
            new ItemStack(BlockRegister.BoxRing3, 1, 0),
            240000,
            (int) TierEU.RECIPE_UEV);
    }

    public void addModuleRecipe() {
        GT_Values.RA.stdBuilder().metadata(RESEARCH_ITEM, new ItemStack(BlockRegister.BoxRing))
            .metadata(RESEARCH_TIME, 48000)
            .itemOutputs(new ItemStack(BlockRegister.BoxRing2, 1, 0))
            .noFluidOutputs()
            .itemInputs(
                GT_OreDictUnificator.get("ringLaurenium", 64),
                GT_OreDictUnificator.get("ringLaurenium", 64),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "particleIon", 32, 21),
                ItemList.ZPM_Coil.get(48))
            .fluidInputs(FluidRegistry.getFluidStack("plasma.titanium", 16000))
            .eut(TierEU.RECIPE_UV)
            .duration(60000)
            .addTo(AssemblyLine);
    }
}
