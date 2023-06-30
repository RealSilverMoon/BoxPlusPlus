package com.silvermoon.boxplusplus.common.loader;

import com.github.technus.tectech.recipe.TT_recipeAdder;
import gregtech.api.enums.*;
import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import static gregtech.api.enums.Mods.*;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sAssemblerRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sMultiblockChemicalRecipes;
import static gregtech.api.util.GT_RecipeConstants.*;

public class RecipeLoader implements Runnable {
    //todo: Add recipes
    public void run() {
        addBoxRecipe();
        addMachineBlockRecipe();
        addModuleRecipe();
        addRingRecipe();
        addUpgradeModuleRecipe();
    }

    public void addBoxRecipe() {
        GT_Values.RA.stdBuilder().metadata(RESEARCH_ITEM,
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 1, 13532))
            .metadata(RESEARCH_TIME, 64000)
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
                ItemList.Field_Generator_IV.get(32),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 1, 15470),
                ItemList.Tool_DataOrb.get(16),
                GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiPart", 1, 360),
                ItemList.Casing_Pipe_Titanium.get(64)
            )
            .fluidInputs(
                FluidRegistry.getFluidStack("molten.titanium", 16416),
                FluidRegistry.getFluidStack("lubricant", 16000),
                FluidRegistry.getFluidStack("ic2uumatter", 4000)
            )
            .eut(TierEU.RECIPE_LuV)
            .duration(8000)
            .addTo(AssemblyLine);
    }

    public void addMachineBlockRecipe() {
        GT_Values.RA.stdBuilder().itemOutputs(new ItemStack(BlockRegister.SpaceExtend, 4, 0))
            .noFluidOutputs()
            .itemInputs(
                ItemList.Casing_StableTitanium.get(64),
                GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 2, 32),
                ItemList.Electric_Motor_LuV.get(2),
                ItemList.Electric_Piston_LuV.get(2),
                GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.IrradiantReinforcedTitaniumPlate", 4))
            .noFluidInputs()
            .eut(TierEU.RECIPE_LuV)
            .duration(400)
            .addTo(sAssemblerRecipes);
        GT_Values.RA.stdBuilder().metadata(RESEARCH_ITEM, new ItemStack(BlockRegister.SpaceExtend))
            .metadata(RESEARCH_TIME, 24000)
            .itemOutputs(new ItemStack(BlockRegister.SpaceCompress, 2, 0))
            .noFluidOutputs()
            .itemInputs(
                ItemList.Casing_StableTitanium.get(64),
                ItemList.Casing_StableTitanium.get(64),
                ItemList.Super_Tank_LV.get(1),
                ItemList.Super_Chest_LV.get(1),
                GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 4, 33),
                GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "tile.TitaniumPlatedReinforcedStone", 64))
            .fluidInputs(
                FluidRegistry.getFluidStack("ic2coolant", 10000),
                FluidRegistry.getFluidStack("molten.indalloy140", 1440)
            )
            .eut(TierEU.RECIPE_LuV)
            .duration(1600)
            .addTo(AssemblyLine);
        GT_Values.RA.stdBuilder().metadata(RESEARCH_ITEM, new ItemStack(BlockRegister.SpaceCompress))
            .metadata(RESEARCH_TIME, 48000)
            .itemOutputs(new ItemStack(BlockRegister.SpaceConstraint, 1, 0))
            .noFluidOutputs()
            .itemInputs(
                ItemList.Casing_StableTitanium.get(64),
                ItemList.Casing_StableTitanium.get(64),
                ItemList.Casing_StableTitanium.get(64),
                ItemList.Casing_StableTitanium.get(64),
                GT_OreDictUnificator.get("plateDoubleAdvancedNitinol", 16),
                GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 16, 34),
                ItemList.Quantum_Tank_LV.get(1),
                ItemList.Quantum_Chest_LV.get(1))
            .fluidInputs(
                FluidRegistry.getFluidStack("supercoolant", 100000),
                FluidRegistry.getFluidStack("molten.indalloy140", 14400))
            .eut(TierEU.RECIPE_UV)
            .duration(6400)
            .addTo(AssemblyLine);
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(BlockRegister.SpaceConstraint),
            480000,
            128,
            2000000,
            8,
            new ItemStack[]{
                GT_ModHandler.getModItem(GTNHIntergalactic.ID, "gt.blockcasingsSE", 64),
                GT_OreDictUnificator.get("blockAstralTitanium", 64),
                GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 64, 34),
                GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1, 60),
                ItemList.Quantum_Tank_IV.get(1),
                ItemList.Quantum_Chest_IV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.pipeHuge, "Titanium", 1)
            },
            new FluidStack[]{
                FluidRegistry.getFluidStack("molten.radoxpoly", 16000),
                FluidRegistry.getFluidStack("molten.chromaticglass", 1440),
                FluidRegistry.getFluidStack("molten.metastable oganesson", 1000),
                FluidRegistry.getFluidStack("molten.titanium", 144)},
            new ItemStack(BlockRegister.SpaceWall, 1),
            12800,
            (int) TierEU.RECIPE_UEV);
    }

    public void addRingRecipe() {
        GT_Values.RA.stdBuilder().itemOutputs(new ItemStack(BlockRegister.BoxRing, 1, 0))
            .noFluidOutputs()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Titanium, 64),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Titanium, 64),
                GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Titanium, 64),
                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Titanium, 64),
                ItemList.Field_Generator_IV.get(32),
                GT_OreDictUnificator.get("circuitUltimate", 4))
            .fluidInputs(FluidRegistry.getFluidStack("molten.tanmolyium beta-c", 51840))
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
                GT_OreDictUnificator.get("ringLaurenium", 64),
                GT_OreDictUnificator.get("ringLaurenium", 64),
                GT_OreDictUnificator.get("blockAdvancedNitinol", 16),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "particleIon", 64, 21),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "particleIon", 64, 21),
                ItemList.Field_Generator_UV.get(4),
                ItemList.ZPM_Coil.get(48))
            .fluidInputs(
                FluidRegistry.getFluidStack("plasma.titanium", 16000),
                FluidRegistry.getFluidStack("molten.advancednitinol", 14400))
            .eut(TierEU.RECIPE_UV)
            .duration(60000)
            .addTo(AssemblyLine);
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(BlockRegister.BoxRing2),
            48000000,
            10240,
            8000000,
            32,
            new ItemStack[]{
                GT_OreDictUnificator.get("ringAstralTitanium", 64),
                GT_OreDictUnificator.get("ringAstralTitanium", 64),
                GT_OreDictUnificator.get("ringAstralTitanium", 64),
                GT_OreDictUnificator.get("ringAstralTitanium", 64),
                GT_OreDictUnificator.get("plateDenseAstralTitanium", 64),
                GT_OreDictUnificator.get("circuitOptical", 32),
                GT_OreDictUnificator.get("batteryUMV", 4),
                ItemList.Field_Generator_UEV.get(4),
                GT_ModHandler.getModItem(UniversalSingularities.ID, "universal.general.singularity", 64, 12),
                GT_ModHandler.getModItem(UniversalSingularities.ID, "universal.general.singularity", 64, 12),
                GT_ModHandler.getModItem(UniversalSingularities.ID, "universal.general.singularity", 64, 12),
                GT_ModHandler.getModItem(UniversalSingularities.ID, "universal.general.singularity", 64, 12),
                GT_OreDictUnificator.get(OrePrefixes.pipeSmall, "Titanium", 1),
                GT_OreDictUnificator.get(OrePrefixes.pipeMedium, "Titanium", 1),
                GT_OreDictUnificator.get(OrePrefixes.pipeLarge, "Titanium", 1),
                GT_OreDictUnificator.get(OrePrefixes.pipeHuge, "Titanium", 1)},
            new FluidStack[]{
                FluidRegistry.getFluidStack("molten.radoxpoly", 640000),
                FluidRegistry.getFluidStack("molten.chromaticglass", 14400),
                FluidRegistry.getFluidStack("molten.metastable oganesson", 32000),
                FluidRegistry.getFluidStack("molten.titanium", 144)},
            new ItemStack(BlockRegister.BoxRing3, 1, 0),
            240000,
            (int) TierEU.RECIPE_UEV);
    }

    //Normally - R1-Assembler; R2-AssemblyLine; R3-ResearchAssemblyLine
    public void addModuleRecipe() {
        GT_Values.RA.stdBuilder()
            .itemOutputs(new ItemStack(BlockRegister.BoxModule, 1, 0))
            .noFluidOutputs()
            .itemInputs(
                new ItemStack(BlockRegister.BoxRing, 1),
                ItemList.Machine_Multi_LargeChemicalReactor.get(64),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 811),
                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Titanium, 16))
            .fluidInputs(
                FluidRegistry.getFluidStack("glue", 8000),
                FluidRegistry.getFluidStack("tetrafluoroethylene", 128000))
            .eut(TierEU.RECIPE_LuV)
            .duration(1200)
            .addTo(sMultiblockChemicalRecipes);
        GT_Values.RA.stdBuilder()
            .itemOutputs(new ItemStack(BlockRegister.BoxModule, 1, 1))
            .noFluidOutputs()
            .itemInputs(
                new ItemStack(BlockRegister.BoxRing, 1),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 876),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 14101),
                GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 64, 43),
                GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 64, 44))
            .fluidInputs(FluidRegistry.getFluidStack("glue", 16000))
            .eut(TierEU.RECIPE_LuV)
            .duration(1200)
            .addTo(sAssemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemOutputs(new ItemStack(BlockRegister.BoxModule, 1, 2))
            .noFluidOutputs()
            .itemInputs(
                new ItemStack(BlockRegister.BoxRing, 1),
                ItemList.OilCracker.get(64),
                ItemList.Distillation_Tower.get(64))
            .fluidInputs(
                FluidRegistry.getFluidStack("glue", 32000),
                FluidRegistry.getFluidStack("highoctanegasoline", 128000))
            .specialValue(5)
            .eut(TierEU.RECIPE_LuV)
            .duration(2400)
            .addTo(GTPP_Recipe.GTPP_Recipe_Map.sChemicalPlantRecipes);
        GT_Values.RA.stdBuilder()
            .itemOutputs(new ItemStack(BlockRegister.BoxModule, 1, 3))
            .noFluidOutputs()
            .itemInputs(
                new ItemStack(BlockRegister.BoxRing, 1),
                ItemList.Machine_Multi_Furnace.get(64),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 849),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 862),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 31023),
                ItemList.Casing_Coil_Cupronickel.get(64),
                ItemList.LuV_Coil.get(64),
                ItemList.Casing_Firebox_Titanium.get(64),
                ItemList.Machine_HV_LightningRod.get(16))
            .fluidInputs(FluidRegistry.getFluidStack("glue", 64000))
            .eut(TierEU.RECIPE_LuV)
            .duration(2400)
            .addTo(sAssemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemOutputs(new ItemStack(BlockRegister.BoxModule, 1, 3))
            .noFluidOutputs()
            .itemInputs(
                new ItemStack(BlockRegister.BoxRing, 1),
                ItemList.Machine_Multi_Furnace.get(64),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 849),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 862),
                ItemList.Casing_Coil_Cupronickel.get(64),
                ItemList.LuV_Coil.get(64),
                ItemList.Casing_Firebox_Titanium.get(64),
                ItemList.Machine_HV_LightningRod.get(16))
            .fluidInputs(FluidRegistry.getFluidStack("glue", 64000))
            .eut(TierEU.RECIPE_LuV)
            .duration(2400)
            .addTo(sAssemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemOutputs(new ItemStack(BlockRegister.BoxModule, 1, 3))
            .noFluidOutputs()
            .itemInputs(
                new ItemStack(BlockRegister.BoxRing, 1),
                ItemList.Machine_Multi_Furnace.get(64),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 849),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 862),
                ItemList.Casing_Coil_Cupronickel.get(64),
                ItemList.LuV_Coil.get(64),
                ItemList.Casing_Firebox_Titanium.get(64),
                ItemList.Machine_HV_LightningRod.get(16))
            .fluidInputs(FluidRegistry.getFluidStack("glue", 64000))
            .eut(TierEU.RECIPE_LuV)
            .duration(2400)
            .addTo(sAssemblerRecipes);
        GT_Values.RA.stdBuilder().metadata(RESEARCH_ITEM,
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 1, 860))
            .metadata(RESEARCH_TIME, 12000)
            .itemOutputs(new ItemStack(BlockRegister.BoxModule, 1, 4))
            .noFluidOutputs()
            .itemInputs(
                new ItemStack(BlockRegister.BoxRing2, 2),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 860),
                GT_ModHandler.getModItem(GregTech.ID, "gt.metaitem.01", 64, 32764),
                GT_ModHandler.getModItem(OpenComputers.ID, "item", 9, 103),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 9, 15470),
                GT_ModHandler.getModItem(OpenComputers.ID, "item", 1, 69),
                GT_ModHandler.getModItem(TecTech.ID, "gt.blockcasingsTT", 9, 3),
                GT_OreDictUnificator.get("wireGt01SuperconductorUV", 9))
            .fluidInputs(
                FluidRegistry.getFluidStack("refinedglue", 8000))
            .eut(TierEU.RECIPE_UV)
            .duration(1200)
            .addTo(AssemblyLine);
        GT_Values.RA.stdBuilder().metadata(RESEARCH_ITEM,
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 1, 31077))
            .metadata(RESEARCH_TIME, 12000)
            .itemOutputs(new ItemStack(BlockRegister.BoxModule, 1, 5))
            .noFluidOutputs()
            .itemInputs(
                new ItemStack(BlockRegister.BoxRing2, 2),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 31077),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 31065),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "MU-metaitem.01", 16, 32044),
                GT_ModHandler.getModItem(ExtraUtilities.ID, "nodeUpgrade", 64, 2),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "dummyResearch", 1),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockcasings9", 4, 1),
                GT_ModHandler.getModItem(Chisel.ID, "netherStarChisel", 1))
            .fluidInputs(
                FluidRegistry.getFluidStack("refinedglue", 8000),
                FluidRegistry.getFluidStack("ic2pahoehoelava", 128000),
                FluidRegistry.getFluidStack("grade4purifiedwater", 128000))
            .eut(TierEU.RECIPE_UV)
            .duration(1200)
            .addTo(AssemblyLine);
        GT_Values.RA.stdBuilder().metadata(RESEARCH_ITEM,
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 1, 992))
            .metadata(RESEARCH_TIME, 12000)
            .itemOutputs(new ItemStack(BlockRegister.BoxModule, 1, 6))
            .noFluidOutputs()
            .itemInputs(
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 792),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 992),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 859),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 797),
                GT_ModHandler.getModItem(AE2Stuff.ID, "Inscriber", 1),
                ItemList.Component_Sawblade_Diamond.get(1),
                ItemList.Shape_Extruder_Ingot.get(1),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "MU-metaitem.01", 1, 32152),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 798),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 31075),
                GT_OreDictUnificator.get("wireGt01SuperconductorUV", 6),
                GT_ModHandler.getModItem(ThaumicBases.ID, "voidAnvil", 16),
                new ItemStack(BlockRegister.BoxRing2, 2))
            .fluidInputs(
                FluidRegistry.getFluidStack("refinedglue", 8000))
            .eut(TierEU.RECIPE_UV)
            .duration(1200)
            .addTo(AssemblyLine);
        GT_Values.RA.stdBuilder().metadata(RESEARCH_ITEM,
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 1, 850))
            .metadata(RESEARCH_TIME, 12000)
            .itemOutputs(new ItemStack(BlockRegister.BoxModule, 1, 7))
            .noFluidOutputs()
            .itemInputs(
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 850),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 796),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 790),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 840),
                GregtechItemList.SimpleDustWasher_UV.get(1),
                GT_OreDictUnificator.get("stickLongNeutronium", 2),
                ItemList.Electric_Motor_UV.get(16),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "gtplusplus.blockcasings.2", 64, 6),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 10862),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 10500),
                GT_OreDictUnificator.get("dustCooledMonaziteRareEarthConcentrate", 64),
                new ItemStack(BlockRegister.BoxRing2, 2))
            .fluidInputs(
                FluidRegistry.getFluidStack("refinedglue", 8000),
                FluidRegistry.getFluidStack("grade4purifiedwater", 128000))
            .eut(TierEU.RECIPE_UV)
            .duration(1200)
            .addTo(AssemblyLine);
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 1, 12730),
            25600,
            64,
            8000000,
            16,
            new ItemStack[]{
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 791),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 12730),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 12731),
                ItemList.Casing_Coil_Cupronickel.get(64),
                GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "tile.QuintupleCompressedCoalCoke", 2),
                GT_OreDictUnificator.get("dustDarkAsh", 64),
                ItemList.Reactor_Coolant_Sp_6.get(1),
                new ItemStack(BlockRegister.BoxRing3, 4),
            },
            new FluidStack[]{
                FluidRegistry.getFluidStack("molten.ethylcyanoacrylatesuperglue", 128000),
                FluidRegistry.getFluidStack("cryotheum", 256000),
                FluidRegistry.getFluidStack("pyrotheum", 256000)},
            new ItemStack(BlockRegister.BoxModule, 1, 8),
            2000,
            (int) TierEU.RECIPE_UEV);
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 1, 17001),
            102400,
            256,
            8000000,
            64,
            new ItemStack[]{
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 15465),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 15145),
                ItemList.Casing_Grate.get(64),
                ItemList.Casing_Grate.get(64),
                ItemList.Casing_Assembler.get(64),
                ItemList.Casing_Assembler.get(64),
                ItemList.Casing_Assembler.get(64),
                ItemList.Casing_Assembler.get(64),
                ItemList.Machine_Multi_Assemblyline.get(64),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 13532),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 13532),
                new ItemStack(BlockRegister.BoxRing3, 4),
                ItemList.Hatch_Input_Multi_2x2_UEV.get(4),
                ItemList.Casing_SolidSteel.get(64),
                ItemList.Casing_SolidSteel.get(64),
                ItemList.Hatch_Output_Bus_ME.get(1)
            },
            new FluidStack[]{
                FluidRegistry.getFluidStack("molten.mutatedlivingsolder", 256000),
                FluidRegistry.getFluidStack("lubricant", 256000)},
            new ItemStack(BlockRegister.BoxModule, 1, 9),
            2000,
            (int) TierEU.RECIPE_UEV);
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 1, 31150),
            51200,
            128,
            8000000,
            32,
            new ItemStack[]{
                new ItemStack(BlockRegister.BoxRing3, 4),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 31150),
                GT_OreDictUnificator.get("blockGlassUMV", 64),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 1, 15497),
                ItemList.Casing_Coil_AwakenedDraconium.get(16),
                ItemList.Casing_Coil_Infinity.get(8),
                ItemList.Casing_Coil_Hypogen.get(4),
                ItemList.Casing_Coil_Eternal.get(2),
                GT_OreDictUnificator.get("wireGt01SuperconductorUIV", 30)
            },
            new FluidStack[]{
                FluidRegistry.getFluidStack("molten.mutatedlivingsolder", 256000),
                FluidRegistry.getFluidStack("lubricant", 256000)},
            new ItemStack(BlockRegister.BoxModule, 1, 10),
            2000,
            (int) TierEU.RECIPE_UEV);
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 1, 15472),
            100000,
            100,
            10000000,
            10,
            new ItemStack[]{
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 15465),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 15465),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 15465),
                ItemList.Sensor_UEV.get(32),
                GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.LaserEmitter", 1),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 1, 15165),
                ItemList.Emitter_UEV.get(64),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 1, 15265),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 15465),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 15465),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 15465),
                ItemList.Sensor_UEV.get(32),
                new ItemStack(BlockRegister.BoxRing3, 16),
            },
            new FluidStack[]{
                FluidRegistry.getFluidStack("supercoolant", 256000)},
            new ItemStack(BlockRegister.BoxModule, 1, 12),
            2000,
            (int) TierEU.RECIPE_UEV);
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            GT_ModHandler.getModItem(AppliedEnergistics2.ID, "tile.BlockAdvancedCraftingUnit", 1, 3),
            (int) TierEU.RECIPE_UEV,
            512,
            (int) TierEU.RECIPE_UIV,
            64,
            new ItemStack[]{
                GT_ModHandler.getModItem(AppliedEnergistics2.ID, "tile.BlockAdvancedCraftingUnit", 64, 3),
                GT_ModHandler.getModItem(AppliedEnergistics2.ID, "tile.BlockAdvancedCraftingUnit", 64, 3),
                GT_ModHandler.getModItem(AppliedEnergistics2.ID, "tile.BlockAdvancedCraftingUnit", 64, 3),
                GT_ModHandler.getModItem(AppliedEnergistics2.ID, "tile.BlockAdvancedCraftingUnit", 64, 3),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockreinforced", 1, 12),
                GT_OreDictUnificator.get("compressedDirt4x", 1),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "blockCompressedObsidian", 1, 4),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "blockCompressedObsidian", 1, 10),
                new ItemStack(BlockRegister.BoxRing3, 32),
            },
            new FluidStack[]{
                FluidRegistry.getFluidStack("grade8purifiedwater", 256000)},
            new ItemStack(BlockRegister.BoxModule, 1, 13),
            2000,
            (int) TierEU.RECIPE_UIV);
    }

    //All - ResearchAssemblyLine
    public void addUpgradeModuleRecipe() {
        GT_Values.RA.stdBuilder().metadata(RESEARCH_ITEM, new ItemStack(BlockRegister.BoxModule, 1, 0))
            .metadata(RESEARCH_TIME, 12000)
            .itemOutputs(new ItemStack(BlockRegister.BoxModuleUpgrad, 1, 0))
            .noFluidOutputs()
            .itemInputs(
                new ItemStack(BlockRegister.BoxRing, 8),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 31072),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 31050),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 31051),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "miscutils.blockcasings", 48, 8),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "gtplusplus.blockspecialcasings.1", 56, 13))
            .fluidInputs(
                FluidRegistry.getFluidStack("refinedglue", 8000),
                FluidRegistry.getFluidStack("ic2uumater", 128000))
            .eut(TierEU.RECIPE_UV)
            .duration(1200)
            .addTo(AssemblyLine);
        GT_Values.RA.stdBuilder().metadata(RESEARCH_ITEM, new ItemStack(BlockRegister.BoxModule, 1, 1))
            .metadata(RESEARCH_TIME, 12000)
            .itemOutputs(new ItemStack(BlockRegister.BoxModuleUpgrad, 1, 1))
            .noFluidOutputs()
            .itemInputs(
                new ItemStack(BlockRegister.BoxRing, 8),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 32018),
                GT_ModHandler.getModItem(GoodGenerator.ID, "preciseUnitCasing", 64, 2),
                GT_ModHandler.getModItem(GoodGenerator.ID, "preciseUnitCasing", 15, 2),
                GT_ModHandler.getModItem(GoodGenerator.ID, "huiCircuit", 64, 1),
                GT_ModHandler.getModItem(GoodGenerator.ID, "huiCircuit", 32, 2),
                GT_ModHandler.getModItem(GoodGenerator.ID, "huiCircuit", 16, 3),
                GT_ModHandler.getModItem(GoodGenerator.ID, "huiCircuit", 8, 4))
            .fluidInputs(
                FluidRegistry.getFluidStack("refinedglue", 8000),
                FluidRegistry.getFluidStack("naquadah based liquid fuel mkii (depleted)", 128000))
            .eut(TierEU.RECIPE_UHV)
            .duration(1200)
            .addTo(AssemblyLine);
        GT_Values.RA.stdBuilder().metadata(RESEARCH_ITEM, new ItemStack(BlockRegister.BoxModule, 1, 2))
            .metadata(RESEARCH_TIME, 12000)
            .itemOutputs(new ItemStack(BlockRegister.BoxModuleUpgrad, 1, 2))
            .noFluidOutputs()
            .itemInputs(
                new ItemStack(BlockRegister.BoxRing, 8),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 998),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "gtplusplus.blockspecialcasings.2", 64, 3),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "gtplusplus.blocktieredcasings.1", 16, 9),
                GT_ModHandler.getModItem(GalacticraftCore.ID, "item.buggy", 1)
            )
            .fluidInputs(
                FluidRegistry.getFluidStack("refinedglue", 8000),
                FluidRegistry.getFluidStack("fluid.rocketfuelmixa", 128000))
            .eut(TierEU.RECIPE_UV)
            .duration(1200)
            .addTo(AssemblyLine);
        GT_Values.RA.stdBuilder().metadata(RESEARCH_ITEM, new ItemStack(BlockRegister.BoxModule, 1, 3))
            .metadata(RESEARCH_TIME, 12000)
            .itemOutputs(new ItemStack(BlockRegister.BoxModuleUpgrad, 1, 3))
            .noFluidOutputs()
            .itemInputs(
                new ItemStack(BlockRegister.BoxRing, 8),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 828),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "gtplusplus.blockcasings.2", 32, 9),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "MU-metaitem.01", 4, 32105),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "itemDustRadioactiveMineralMix", 1))
            .fluidInputs(
                FluidRegistry.getFluidStack("refinedglue", 8000),
                FluidRegistry.getFluidStack("plasma.hydrogen", 12800))
            .eut(TierEU.RECIPE_UV)
            .duration(1200)
            .addTo(AssemblyLine);
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(BlockRegister.BoxModule, 1, 4),
            256000,
            512,
            8000000,
            16,
            new ItemStack[]{
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 356),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 12735),
                GT_ModHandler.getModItem(BartWorks.ID, "gt.bwMetaGeneratedItem0", 64, 3),
                GT_OreDictUnificator.get("gemExquisitePrasiolite", 64),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockcasings8", 8, 14),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockcasings8", 16, 10),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockcasings8", 32, 12),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockcasings8", 64, 13),
                ItemList.Circuit_Parts_ResistorXSMD.get(64),
                ItemList.Circuit_Parts_CapacitorXSMD.get(64),
                ItemList.Circuit_Parts_DiodeXSMD.get(64),
                ItemList.Circuit_Parts_TransistorXSMD.get(64),
                GT_ModHandler.getModItem(GregTech.ID, "gt.metaitem.03", 64, 32728),
                GT_ModHandler.getModItem(GregTech.ID, "gt.metaitem.03", 64, 32107),
                GT_ModHandler.getModItem(GregTech.ID, "gt.metaitem.03", 64, 32105),
                new ItemStack(BlockRegister.BoxRing2, 16)
            },
            new FluidStack[]{
                FluidRegistry.getFluidStack("molten.ethylcyanoacrylatesuperglue", 128000),
                FluidRegistry.getFluidStack("supercoolant", 320000)},
            new ItemStack(BlockRegister.BoxModuleUpgrad, 1, 4),
            2000,
            (int) TierEU.RECIPE_UIV);
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(BlockRegister.BoxModule, 1, 5),
            128000,
            256,
            8000000,
            4,
            new ItemStack[]{
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 965),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 975),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 8, 32022),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 8, 32023),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "gtplusplus.blockcasings.3", 64, 13),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "gtplusplus.blockcasings.6", 64, 1),
                GT_ModHandler.getModItem(GoodGenerator.ID, "compactFusionCoil", 24, 2),
                GT_ModHandler.getModItem(GoodGenerator.ID, "compactFusionCoil", 24, 4),
                GT_OreDictUnificator.get("batteryMAX", 1),
                new ItemStack(BlockRegister.BoxRing2, 16)
            },
            new FluidStack[]{
                FluidRegistry.getFluidStack("molten.ethylcyanoacrylatesuperglue", 128000),
                FluidRegistry.getFluidStack("molten.metastable oganesson", 64000),
                FluidRegistry.getFluidStack("plasma.radon", 288000)},
            new ItemStack(BlockRegister.BoxModuleUpgrad, 1, 5),
            2000,
            (int) TierEU.RECIPE_UEV);
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(BlockRegister.BoxModule, 1, 6),
            64000,
            128,
            8000000,
            1,
            new ItemStack[]{
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 1132),
                ItemList.Electric_Motor_UEV.get(32),
                ItemList.Electric_Piston_UEV.get(8),
                ItemList.Electric_Pump_UEV.get(16),
                ItemList.Conveyor_Module_UEV.get(8),
                ItemList.Robot_Arm_UEV.get(8),
                GT_OreDictUnificator.get("circuitOptical", 4),
                GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemExtremeStorageCell.Singularity", 1),
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Titanium, 32),
                GT_OreDictUnificator.get("rotorAstralTitanium", 16),
                new ItemStack(BlockRegister.BoxRing2, 16),
            },
            new FluidStack[]{
                FluidRegistry.getFluidStack("lubricant", 128000),
                FluidRegistry.getFluidStack("grade7purifiedwater", 256000)},
            new ItemStack(BlockRegister.BoxModuleUpgrad, 1, 6),
            2000,
            (int) TierEU.RECIPE_UEV);
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(BlockRegister.BoxModule, 1, 7),
            10240,
            16,
            2000000,
            1,
            new ItemStack[]{
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 10501),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 12712),
                GT_OreDictUnificator.get("blockGlassUEV", 64),
                GT_OreDictUnificator.get("slabWood", 1),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 12718),
                GT_ModHandler.getModItem(UniversalSingularities.ID, "universal.general.singularity", 4, 26),
                new ItemStack(BlockRegister.BoxRing2, 16),
            },
            new FluidStack[]{
                FluidRegistry.getFluidStack("molten.ethylcyanoacrylatesuperglue", 128000),
                FluidRegistry.getFluidStack("grade6purifiedwater", 256000),
                FluidRegistry.getFluidStack("xenoxene", 128000),
                FluidRegistry.getFluidStack("bacterialsludge", 24000)},
            new ItemStack(BlockRegister.BoxModuleUpgrad, 1, 7),
            2000,
            (int) TierEU.RECIPE_UHV);
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(BlockRegister.BoxModule, 1, 8),
            81920000,
            32768,
            (int) TierEU.RECIPE_UMV,
            16,
            new ItemStack[]{
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 357),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 1006),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 1004),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 1, 15310),
                GT_OreDictUnificator.get("naniteWhiteDwarfMatter", 1),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 581),
                ItemList.Casing_Coil_Eternal.get(64),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 3, 15470),
                GT_ModHandler.getModItem(TecTech.ID, "gt.blockcasingsBA0", 32, 10),
                GT_ModHandler.getModItem(TecTech.ID, "gt.blockcasingsBA0", 32, 11),
                GT_ModHandler.getModItem(TecTech.ID, "gt.stabilisation_field_generator", 1),
                new ItemStack(BlockRegister.BoxRing3, 32),
            },
            new FluidStack[]{
                FluidRegistry.getFluidStack("molten.ethylcyanoacrylatesuperglue", 10240000),
                FluidRegistry.getFluidStack("primordialmatter", 8000),
                FluidRegistry.getFluidStack("exciteddtsc", 8000),
                FluidRegistry.getFluidStack("molten.spacetime", 24000)},
            new ItemStack(BlockRegister.BoxModuleUpgrad, 1, 8),
            2000,
            (int) TierEU.RECIPE_UXV);
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(BlockRegister.BoxModule, 1, 9),
            10240000,
            6144,
            (int) TierEU.RECIPE_UIV,
            16,
            new ItemStack[]{
                GT_ModHandler.getModItem(GoodGenerator.ID, "componentAssemblylineCasing", 16, 32023),
                GT_ModHandler.getModItem(GoodGenerator.ID, "componentAssemblylineCasing", 16, 32023),
                GT_OreDictUnificator.get("blockSpaceTime", 4),
                GT_OreDictUnificator.get("blockSpaceTime", 8),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 32026),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 12734),
                GT_OreDictUnificator.get("blockTranscendentMetal", 4),
                GT_OreDictUnificator.get("blockSpaceTime", 16),
                GT_ModHandler.getModItem(AppliedEnergistics2.ID, "tile.BlockTinyTNT", 1),
                GT_ModHandler.getModItem(AppliedEnergistics2.ID, "tile.BlockTinyTNT", 1),
                GT_OreDictUnificator.get("blockSpaceTime", 4),
                GT_OreDictUnificator.get("blockSpaceTime", 8),
                new ItemStack(BlockRegister.BoxRing3, 32),
            },
            new FluidStack[]{
                FluidRegistry.getFluidStack("molten.ethylcyanoacrylatesuperglue", 10240000),
                FluidRegistry.getFluidStack("glyceryl", 128000),
                FluidRegistry.getFluidStack("molten.mutatedlivingsolder", 256000),
                FluidRegistry.getFluidStack("lubricant", 256000)},
            new ItemStack(BlockRegister.BoxModuleUpgrad, 1, 9),
            2000,
            (int) TierEU.RECIPE_UIV);
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(BlockRegister.BoxModule, 1, 10),
            20480000,
            10240,
            (int) TierEU.RECIPE_UMV,
            16,
            new ItemStack[]{
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 31151),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 16999),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "gtplusplus.blockcasings.5", 64, 14),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "gtplusplus.blockcasings.5", 64, 10),
                GT_ModHandler.getModItem(GTPlusPlus.ID, "gtplusplus.blockcasings.4", 64, 4),
                GT_ModHandler.getModItem(GoodGenerator.ID, "FRF_Coil_3", 64),
                GT_OreDictUnificator.get("lensOrundum", 1),
                GT_OreDictUnificator.get("rotorExtremelyUnstableNaquadah", 16),
                ItemList.Timepiece.get(1),
                ItemList.NaquadriaSupersolid.get(1),
                ItemList.SuperconductorComposite.get(1),
                ItemList.StableAdhesive.get(1),
                new ItemStack(BlockRegister.BoxRing3, 32),
            },
            new FluidStack[]{
                FluidRegistry.getFluidStack("molten.ethylcyanoacrylatesuperglue", 10240000),
                FluidRegistry.getFluidStack("naquadah based liquid fuel mk v (depleted)", 128000),
                FluidRegistry.getFluidStack("molten.eternity", 256000),
                FluidRegistry.getFluidStack("temporalfluid", 256000)},
            new ItemStack(BlockRegister.BoxModuleUpgrad, 1, 10),
            2000,
            (int) TierEU.RECIPE_UXV);
        CraftingManager.getInstance().addRecipe(
            new ItemStack(BlockRegister.BoxModuleUpgrad, 1, 12),
            "ABA",
            "FCF",
            "DED",
            'A',
            GT_ModHandler.getModItem(GalaxySpace.ID, "item.DysonSwarmParts", 1),
            'B',
            GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 14001),
            'C',
            GT_ModHandler.getModItem(GalacticraftMars.ID, "tile.marsMachine", 1, 4),
            'D',
            GT_ModHandler.getModItem(GalacticraftAmunRa.ID, "tile.machines2", 1, 1),
            'E',
            new ItemStack(BlockRegister.BoxModule, 1, 10),
            'F',
            GT_ModHandler.getModItem(TecTech.ID, "gt.stabilisation_field_generator", 1, 8)
        );
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(BlockRegister.BoxModule, 1, 13),
            (int)TierEU.UXV,
            32767,
            (int)TierEU.UXV,
            64,
            new ItemStack[]{
                ItemList.Electric_Motor_UXV.get(64),
                ItemList.Electric_Pump_UXV.get(64),
                ItemList.Emitter_UXV.get(64),
                ItemList.Electric_Piston_UXV.get(64),
                ItemList.Sensor_UXV.get(64),
                ItemList.Robot_Arm_UXV.get(64),
                ItemList.GigaChad.get(1),
                GT_ModHandler.getModItem(TecTech.ID, "gt.stabilisation_field_generator", 48, 8),
                GT_OreDictUnificator.get("batteryERV", 16),
                new ItemStack(BlockRegister.BoxRing3, 64),
                new ItemStack(BlockRegister.BoxRing3, 64),
                GT_OreDictUnificator.get("ingotTitanium", 1),
                GT_OreDictUnificator.get("ingotSilver", 1)
            },
            new FluidStack[]{
                FluidRegistry.getFluidStack("molten.magnetohydrodynamicallyconstrainedstarmatter", 20000000),
                FluidRegistry.getFluidStack("molten.shirabon", 20000000),
                FluidRegistry.getFluidStack("molten.titanium", 20000000),
                FluidRegistry.getFluidStack("molten.silver", 20000000)},
            new ItemStack(BlockRegister.BoxModuleUpgrad, 1, 13),
            Integer.MAX_VALUE - 1,
            Integer.MAX_VALUE - 1);
        //This is the debug test module recipe, maybe oneday we can re-add it...
        /*TT_recipeAdder.addResearchableAssemblylineRecipe(
            GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 1, 1145),
            Integer.MAX_VALUE - 1,
            32767,
            Integer.MAX_VALUE - 1,
            96,
            new ItemStack[]{
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines", 4, 1145),
                ItemList.Electric_Motor_UXV.get(64),
                ItemList.Electric_Pump_UXV.get(64),
                ItemList.Emitter_UXV.get(64),
                ItemList.Electric_Piston_UXV.get(64),
                ItemList.Sensor_UXV.get(64),
                ItemList.Robot_Arm_UXV.get(64),
                ItemList.GigaChad.get(1),
                GT_ModHandler.getModItem(SGCraft.ID, "stargateRing", 8, 0),
                GT_ModHandler.getModItem(SGCraft.ID, "stargateRing", 4, 1),
                GT_ModHandler.getModItem(SGCraft.ID, "stargateBase", 1),
                GT_OreDictUnificator.get("batteryERV", 64),
                new ItemStack(BlockRegister.BoxRing3, 64),
                new ItemStack(BlockRegister.BoxRing3, 64),
                GT_OreDictUnificator.get("ingotTitanium", 1),
                GT_OreDictUnificator.get("ingotSilver", 1)
            },
            new FluidStack[]{
                FluidRegistry.getFluidStack("molten.magnetohydrodynamicallyconstrainedstarmatter", 2000000000),
                FluidRegistry.getFluidStack("molten.shirabon", 2000000000),
                FluidRegistry.getFluidStack("molten.titanium", 2000000000),
                FluidRegistry.getFluidStack("molten.silver", 2000000000)},
            new ItemStack(BlockRegister.BoxModuleUpgrad, 1, 13),
            Integer.MAX_VALUE - 1,
            Integer.MAX_VALUE - 1);*/
    }
}
