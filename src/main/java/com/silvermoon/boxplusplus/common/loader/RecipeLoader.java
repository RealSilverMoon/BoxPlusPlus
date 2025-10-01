package com.silvermoon.boxplusplus.common.loader;

import static gregtech.api.enums.Mods.*;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.multiblockChemicalReactorRecipes;
import static gregtech.api.util.GTRecipeConstants.*;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.*;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.recipe.Scanning;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import tectech.recipe.TTRecipeAdder;

public class RecipeLoader implements Runnable {

    public synchronized void run() {
        addBoxRecipe();
        addMachineBlockRecipe();
        addModuleRecipe();
        addRingRecipe();
        addUpgradeModuleRecipe();
        addModuleResearchRecipe();
    }

    public static void addBoxRecipe() {
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 1, 13532))
            .metadata(SCANNING, new Scanning(64000, TierEU.RECIPE_LV))
            .itemOutputs(TileEntitiesLoader.Box.getStackForm(1))
            .itemInputs(
                new ItemStack(BlockRegister.BoxRing, 1, 0),
                GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 4, 59),
                GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 8, 58),
                GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 16, 57),
                GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 32, 38),
                GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 64, 37),
                new ItemStack(BlockRegister.SpaceExtend, 64, 0),
                new ItemStack(BlockRegister.SpaceExtend, 64, 0),
                new ItemStack(BlockRegister.SpaceExtend, 64, 0),
                new ItemStack(BlockRegister.SpaceExtend, 64, 0),
                ItemList.Field_Generator_IV.get(4),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 1, 15470),
                ItemList.Tool_DataOrb.get(16),
                GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiPart", 1, 360),
                ItemList.Casing_Pipe_Titanium.get(64))
            .fluidInputs(
                FluidRegistry.getFluidStack("molten.titanium", 16416),
                FluidRegistry.getFluidStack("lubricant", 16000),
                FluidRegistry.getFluidStack("ic2uumatter", 4000))
            .eut(TierEU.RECIPE_LuV)
            .duration(8000)
            .addTo(AssemblyLine);
    }

    public static void addMachineBlockRecipe() {
        GTValues.RA.stdBuilder()
            .itemOutputs(new ItemStack(BlockRegister.SpaceExtend, 32, 0))
            .itemInputs(
                ItemList.Casing_StableTitanium.get(64),
                GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 2, 32),
                ItemList.Electric_Motor_LuV.get(16),
                ItemList.Electric_Piston_LuV.get(16),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.IrradiantReinforcedTitaniumPlate", 4))
            .eut(TierEU.RECIPE_LuV)
            .duration(400)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, new ItemStack(BlockRegister.SpaceExtend))
            .metadata(SCANNING, new Scanning(24000, TierEU.RECIPE_LV))
            .itemOutputs(new ItemStack(BlockRegister.SpaceCompress, 16, 0))
            .itemInputs(
                ItemList.Casing_StableTitanium.get(64),
                ItemList.Casing_StableTitanium.get(64),
                ItemList.Super_Tank_LV.get(1),
                ItemList.Super_Chest_LV.get(1),
                GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 4, 33),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "tile.TitaniumPlatedReinforcedStone", 64))
            .fluidInputs(
                FluidRegistry.getFluidStack("ic2coolant", 10000),
                FluidRegistry.getFluidStack("molten.indalloy140", 1440))
            .eut(TierEU.RECIPE_LuV)
            .duration(1600)
            .addTo(AssemblyLine);
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, new ItemStack(BlockRegister.SpaceCompress))
            .metadata(SCANNING, new Scanning(48000, TierEU.RECIPE_LV))
            .itemOutputs(new ItemStack(BlockRegister.SpaceConstraint, 2, 0))
            .itemInputs(
                ItemList.Casing_StableTitanium.get(64),
                ItemList.Casing_StableTitanium.get(64),
                ItemList.Casing_StableTitanium.get(64),
                ItemList.Casing_StableTitanium.get(64),
                GTOreDictUnificator.get("plateDoubleAdvancedNitinol", 8),
                GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 16, 34),
                ItemList.Quantum_Tank_LV.get(1),
                ItemList.Quantum_Chest_LV.get(1))
            .fluidInputs(
                FluidRegistry.getFluidStack("supercoolant", 100000),
                FluidRegistry.getFluidStack("molten.indalloy140", 14400))
            .eut(TierEU.RECIPE_UV)
            .duration(6400)
            .addTo(AssemblyLine);
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(BlockRegister.SpaceConstraint),
            480000,
            128,
            2000000,
            8,
            new ItemStack[] { GTModHandler.getModItem(GTNHIntergalactic.ID, "gt.blockcasingsSE", 64),
                GTOreDictUnificator.get("blockAstralTitanium", 48),
                GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 32, 34),
                GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1, 60),
                ItemList.Quantum_Tank_IV.get(1), ItemList.Quantum_Chest_IV.get(1),
                GTOreDictUnificator.get(OrePrefixes.pipeHuge, "Titanium", 1) },
            new FluidStack[] { FluidRegistry.getFluidStack("molten.radoxpoly", 576),
                FluidRegistry.getFluidStack("molten.chromaticglass", 1440),
                FluidRegistry.getFluidStack("molten.metastable oganesson", 1000),
                FluidRegistry.getFluidStack("molten.titanium", 144) },
            new ItemStack(BlockRegister.SpaceWall, 1),
            12800,
            (int) TierEU.RECIPE_UEV);
    }

    public static void addRingRecipe() {
        GTValues.RA.stdBuilder()
            .itemOutputs(new ItemStack(BlockRegister.BoxRing, 1, 0))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.Titanium, 64),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.Titanium, 64),
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Titanium, 64),
                GTOreDictUnificator.get(OrePrefixes.rotor, Materials.Titanium, 64),
                ItemList.Field_Generator_IV.get(4),
                new Object[] { OrePrefixes.circuit.get(Materials.Ultimate), 4 })
            .fluidInputs(FluidRegistry.getFluidStack("molten.tanmolyium beta-c", 51840))
            .eut(TierEU.RECIPE_LuV)
            .duration(1200)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, new ItemStack(BlockRegister.BoxRing))
            .metadata(SCANNING, new Scanning(48000, TierEU.RECIPE_LV))
            .itemOutputs(new ItemStack(BlockRegister.BoxRing2, 1, 0))
            .itemInputs(
                GTOreDictUnificator.get("ringLaurenium", 64),
                GTOreDictUnificator.get("ringLaurenium", 64),
                GTOreDictUnificator.get("ringLaurenium", 64),
                GTOreDictUnificator.get("ringLaurenium", 64),
                GTOreDictUnificator.get("blockAdvancedNitinol", 16),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Tritanium, 64),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Tritanium, 64),
                ItemList.Field_Generator_UV.get(4),
                ItemList.ZPM_Coil.get(48))
            .fluidInputs(
                FluidRegistry.getFluidStack("plasma.titanium", 16000),
                FluidRegistry.getFluidStack("molten.advancednitinol", 14400))
            .eut(TierEU.RECIPE_UV)
            .duration(2000)
            .addTo(AssemblyLine);
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(BlockRegister.BoxRing2),
            48000000,
            10240,
            8000000,
            32,
            new Object[] { GTOreDictUnificator.get("ringAstralTitanium", 64),
                GTOreDictUnificator.get("ringAstralTitanium", 64), GTOreDictUnificator.get("ringAstralTitanium", 64),
                GTOreDictUnificator.get("ringAstralTitanium", 64),
                GTOreDictUnificator.get("plateDenseAstralTitanium", 64),
                new Object[] { OrePrefixes.circuit.get(Materials.UIV), 32 }, GTOreDictUnificator.get("batteryUMV", 4),
                ItemList.Field_Generator_UEV.get(4),
                GTModHandler.getModItem(UniversalSingularities.ID, "universal.general.singularity", 64, 12),
                GTModHandler.getModItem(UniversalSingularities.ID, "universal.general.singularity", 64, 12),
                GTModHandler.getModItem(UniversalSingularities.ID, "universal.general.singularity", 64, 12),
                GTModHandler.getModItem(UniversalSingularities.ID, "universal.general.singularity", 64, 12),
                GTOreDictUnificator.get(OrePrefixes.pipeSmall, "Titanium", 1),
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, "Titanium", 1),
                GTOreDictUnificator.get(OrePrefixes.pipeLarge, "Titanium", 1),
                GTOreDictUnificator.get(OrePrefixes.pipeHuge, "Titanium", 1) },
            new FluidStack[] { FluidRegistry.getFluidStack("molten.radoxpoly", 640000),
                FluidRegistry.getFluidStack("molten.chromaticglass", 14400),
                FluidRegistry.getFluidStack("molten.metastable oganesson", 32000),
                FluidRegistry.getFluidStack("molten.titanium", 144) },
            new ItemStack(BlockRegister.BoxRing3, 1, 0),
            800,
            (int) TierEU.RECIPE_UEV);
    }

    // Normally - R1-Assembler; R2-AssemblyLine; R3-ResearchAssemblyLine
    public static void addModuleRecipe() {
        GTValues.RA.stdBuilder()
            .itemOutputs(new ItemStack(BlockRegister.BoxModule, 1, 0))
            .itemInputs(
                new ItemStack(BlockRegister.BoxRing, 1),
                ItemList.Machine_Multi_LargeChemicalReactor.get(64),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 811),
                GTOreDictUnificator.get(OrePrefixes.rotor, Materials.Titanium, 16))
            .fluidInputs(
                FluidRegistry.getFluidStack("glue", 8000),
                FluidRegistry.getFluidStack("tetrafluoroethylene", 128000))
            .eut(TierEU.RECIPE_LuV)
            .duration(1200)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .itemOutputs(new ItemStack(BlockRegister.BoxModule, 1, 1))
            .itemInputs(
                new ItemStack(BlockRegister.BoxRing, 1),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 876),
                GTModHandler.getModItem(Avaritia.ID, "Triple_Craft", 64),
                GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 64, 43),
                GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 64, 44))
            .fluidInputs(FluidRegistry.getFluidStack("glue", 16000))
            .eut(TierEU.RECIPE_LuV)
            .duration(1200)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemOutputs(new ItemStack(BlockRegister.BoxModule, 1, 2))
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
            .addTo(GTPPRecipeMaps.chemicalPlantRecipes);
        GTValues.RA.stdBuilder()
            .itemOutputs(new ItemStack(BlockRegister.BoxModule, 1, 3))
            .itemInputs(
                new ItemStack(BlockRegister.BoxRing, 1),
                ItemList.Machine_Multi_Furnace.get(64),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 849),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 862),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 31023),
                ItemList.Casing_Coil_Cupronickel.get(64),
                ItemList.LuV_Coil.get(64),
                ItemList.Casing_Firebox_Titanium.get(64),
                ItemList.Machine_HV_LightningRod.get(16))
            .fluidInputs(FluidRegistry.getFluidStack("glue", 64000))
            .eut(TierEU.RECIPE_LuV)
            .duration(2400)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, new ItemStack(ItemRegister.bmResearchItem, 1, 0))
            .metadata(SCANNING, new Scanning(12000, TierEU.RECIPE_LV))
            .itemOutputs(new ItemStack(BlockRegister.BoxModule, 1, 4))
            .itemInputs(
                new ItemStack(BlockRegister.BoxRing2, 2),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 860),
                GTModHandler.getModItem(GregTech.ID, "gt.metaitem.01", 64, 32764),
                GTModHandler.getModItem(OpenComputers.ID, "item", 9, 103),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 9, 15470),
                GTModHandler.getModItem(OpenComputers.ID, "item", 1, 69),
                GTModHandler.getModItem(TecTech.ID, "gt.blockcasingsTT", 9, 3),
                GTOreDictUnificator.get("wireGt01SuperconductorUV", 9))
            .fluidInputs(FluidRegistry.getFluidStack("refinedglue", 8000))
            .eut(TierEU.RECIPE_UV)
            .duration(1200)
            .addTo(AssemblyLine);
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, new ItemStack(ItemRegister.bmResearchItem, 1, 1))
            .metadata(SCANNING, new Scanning(12000, TierEU.RECIPE_LV))
            .itemOutputs(new ItemStack(BlockRegister.BoxModule, 1, 5))
            .itemInputs(
                new ItemStack(BlockRegister.BoxRing2, 2),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 31077),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 31065),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 4, 3007),
                GTModHandler.getModItem(GTPlusPlus.ID, "MU-metaitem.01", 16, 32044),
                GTModHandler.getModItem(ExtraUtilities.ID, "nodeUpgrade", 64, 2),
                GTModHandler.getModItem(GTPlusPlus.ID, "dummyResearch", 1),
                GTModHandler.getModItem(GregTech.ID, "gt.blockcasings9", 4, 1),
                GTModHandler.getModItem(Chisel.ID, "netherStarChisel", 1))
            .fluidInputs(
                FluidRegistry.getFluidStack("refinedglue", 8000),
                FluidRegistry.getFluidStack("ic2pahoehoelava", 128000),
                FluidRegistry.getFluidStack("grade4purifiedwater", 128000))
            .eut(TierEU.RECIPE_UV)
            .duration(1200)
            .addTo(AssemblyLine);
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, new ItemStack(ItemRegister.bmResearchItem, 1, 2))
            .metadata(SCANNING, new Scanning(12000, TierEU.RECIPE_LV))
            .itemOutputs(new ItemStack(BlockRegister.BoxModule, 1, 6))
            .itemInputs(
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 792),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 992),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 859),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 797),
                GTModHandler.getModItem(AE2Stuff.ID, "Inscriber", 1),
                ItemList.Component_Sawblade_Diamond.get(1),
                ItemList.Shape_Extruder_Ingot.get(1),
                GTModHandler.getModItem(GTPlusPlus.ID, "MU-metaitem.01", 1, 32152),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 798),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 31075),
                GTOreDictUnificator.get("wireGt01SuperconductorUV", 6),
                GTModHandler.getModItem(ThaumicBases.ID, "voidAnvil", 16),
                new ItemStack(BlockRegister.BoxRing2, 2))
            .fluidInputs(FluidRegistry.getFluidStack("refinedglue", 8000))
            .eut(TierEU.RECIPE_UV)
            .duration(1200)
            .addTo(AssemblyLine);
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, new ItemStack(ItemRegister.bmResearchItem, 1, 3))
            .metadata(SCANNING, new Scanning(12000, TierEU.RECIPE_LV))
            .itemOutputs(new ItemStack(BlockRegister.BoxModule, 1, 7))
            .itemInputs(
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 850),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 796),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 790),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 840),
                GregtechItemList.SimpleDustWasher_UV.get(1),
                GTOreDictUnificator.get("stickLongNeutronium", 2),
                ItemList.Electric_Motor_UV.get(16),
                GTModHandler.getModItem(GTPlusPlus.ID, "gtplusplus.blockcasings.2", 64, 6),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 10862),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 10500),
                GTOreDictUnificator.get("dustCooledMonaziteRareEarthConcentrate", 64),
                new ItemStack(BlockRegister.BoxRing2, 2))
            .fluidInputs(
                FluidRegistry.getFluidStack("refinedglue", 8000),
                FluidRegistry.getFluidStack("grade4purifiedwater", 128000))
            .eut(TierEU.RECIPE_UV)
            .duration(1200)
            .addTo(AssemblyLine);
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(ItemRegister.bmResearchItem, 1, 4),
            25600,
            64,
            8000000,
            16,
            new ItemStack[] { GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 791),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 12730),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 12731),
                ItemList.Casing_Coil_Cupronickel.get(64),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "tile.QuintupleCompressedCoalCoke", 2),
                GTOreDictUnificator.get("dustDarkAsh", 64), ItemList.Reactor_Coolant_Sp_6.get(1),
                new ItemStack(BlockRegister.BoxRing3, 4), },
            new FluidStack[] { FluidRegistry.getFluidStack("molten.ethylcyanoacrylatesuperglue", 128000),
                FluidRegistry.getFluidStack("cryotheum", 256000), FluidRegistry.getFluidStack("pyrotheum", 256000) },
            new ItemStack(BlockRegister.BoxModule, 1, 8),
            2000,
            (int) TierEU.RECIPE_UEV);
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(ItemRegister.bmResearchItem, 1, 5),
            102400,
            256,
            8000000,
            64,
            new ItemStack[] { GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 15465),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 15145), ItemList.Casing_Grate.get(64),
                ItemList.Casing_Grate.get(64), ItemList.Casing_Assembler.get(64), ItemList.Casing_Assembler.get(64),
                ItemList.Casing_Assembler.get(64), ItemList.Casing_Assembler.get(64),
                ItemList.Machine_Multi_Assemblyline.get(64),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 13532),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 13532),
                new ItemStack(BlockRegister.BoxRing3, 4), ItemList.Hatch_Input_Multi_2x2_UEV.get(4),
                ItemList.Casing_SolidSteel.get(64), ItemList.Casing_SolidSteel.get(64),
                ItemList.Hatch_Output_Bus_ME.get(1) },
            new FluidStack[] { FluidRegistry.getFluidStack("molten.mutatedlivingsolder", 256000),
                FluidRegistry.getFluidStack("lubricant", 256000) },
            new ItemStack(BlockRegister.BoxModule, 1, 9),
            2000,
            (int) TierEU.RECIPE_UEV);
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(ItemRegister.bmResearchItem, 1, 6),
            51200,
            128,
            8000000,
            32,
            new ItemStack[] { new ItemStack(BlockRegister.BoxRing3, 4),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 31150),
                GTOreDictUnificator.get("blockGlassUMV", 64),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 1, 15497),
                ItemList.Casing_Coil_AwakenedDraconium.get(16), ItemList.Casing_Coil_Infinity.get(8),
                ItemList.Casing_Coil_Hypogen.get(4), ItemList.Casing_Coil_Eternal.get(2),
                GTOreDictUnificator.get("wireGt01SuperconductorUIV", 30) },
            new FluidStack[] { FluidRegistry.getFluidStack("molten.mutatedlivingsolder", 256000),
                FluidRegistry.getFluidStack("lubricant", 256000) },
            new ItemStack(BlockRegister.BoxModule, 1, 10),
            2000,
            (int) TierEU.RECIPE_UEV);
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(ItemRegister.bmResearchItem, 1, 7),
            100000,
            100,
            10000000,
            10,
            new ItemStack[] { GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 15465),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 15465),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 15465), ItemList.Sensor_UEV.get(32),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.LaserEmitter", 1),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 1, 15165), ItemList.Emitter_UEV.get(64),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 1, 15265),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 15465),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 15465),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 15465), ItemList.Sensor_UEV.get(32),
                new ItemStack(BlockRegister.BoxRing3, 16), },
            new FluidStack[] { FluidRegistry.getFluidStack("supercoolant", 256000) },
            new ItemStack(BlockRegister.BoxModule, 1, 12),
            2000,
            (int) TierEU.RECIPE_UEV);
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(ItemRegister.bmResearchItem, 1, 8),
            (int) TierEU.RECIPE_UEV,
            512,
            (int) TierEU.RECIPE_UIV,
            64,
            new ItemStack[] { GTModHandler.getModItem(AppliedEnergistics2.ID, "tile.BlockAdvancedCraftingUnit", 64, 3),
                GTModHandler.getModItem(AppliedEnergistics2.ID, "tile.BlockAdvancedCraftingUnit", 64, 3),
                GTModHandler.getModItem(AppliedEnergistics2.ID, "tile.BlockAdvancedCraftingUnit", 64, 3),
                GTModHandler.getModItem(AppliedEnergistics2.ID, "tile.BlockAdvancedCraftingUnit", 64, 3),
                GTModHandler.getModItem(GregTech.ID, "gt.blockreinforced", 1, 12),
                GTOreDictUnificator.get("compressedDirt4x", 1),
                GTModHandler.getModItem(GTPlusPlus.ID, "blockCompressedObsidian", 1, 4),
                GTModHandler.getModItem(GTPlusPlus.ID, "blockCompressedObsidian", 1, 10),
                new ItemStack(BlockRegister.BoxRing3, 32), },
            new FluidStack[] { FluidRegistry.getFluidStack("grade8purifiedwater", 256000) },
            new ItemStack(BlockRegister.BoxModule, 1, 13),
            2000,
            (int) TierEU.RECIPE_UIV);
    }

    // All - ResearchAssemblyLine
    public static void addUpgradeModuleRecipe() {
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, new ItemStack(BlockRegister.BoxModule, 1, 0))
            .metadata(SCANNING, new Scanning(12000, TierEU.RECIPE_LV))
            .itemOutputs(new ItemStack(BlockRegister.BoxModuleUpgrad, 1, 0))
            .itemInputs(
                new ItemStack(BlockRegister.BoxRing, 8),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 31072),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 31050),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 31051),
                GTModHandler.getModItem(GTPlusPlus.ID, "miscutils.blockcasings", 48, 8),
                GTModHandler.getModItem(GTPlusPlus.ID, "gtplusplus.blockspecialcasings.1", 56, 13))
            .fluidInputs(
                FluidRegistry.getFluidStack("refinedglue", 8000),
                FluidRegistry.getFluidStack("ic2uumatter", 128000))
            .eut(TierEU.RECIPE_UV)
            .duration(1200)
            .addTo(AssemblyLine);
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, new ItemStack(BlockRegister.BoxModule, 1, 1))
            .metadata(SCANNING, new Scanning(12000, TierEU.RECIPE_LV))
            .itemOutputs(new ItemStack(BlockRegister.BoxModuleUpgrad, 1, 1))
            .itemInputs(
                new ItemStack(BlockRegister.BoxRing, 8),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 32018),
                GTModHandler.getModItem(GoodGenerator.ID, "preciseUnitCasing", 64, 2),
                GTModHandler.getModItem(Avaritia.ID, "Dire_Crafting", 64),
                GTModHandler.getModItem(GoodGenerator.ID, "huiCircuit", 64, 1),
                GTModHandler.getModItem(GoodGenerator.ID, "huiCircuit", 32, 2),
                GTModHandler.getModItem(GoodGenerator.ID, "huiCircuit", 16, 3),
                GTModHandler.getModItem(GoodGenerator.ID, "huiCircuit", 8, 4))
            .fluidInputs(
                FluidRegistry.getFluidStack("refinedglue", 8000),
                FluidRegistry.getFluidStack("naquadah based liquid fuel mkii (depleted)", 128000))
            .eut(TierEU.RECIPE_UHV)
            .duration(1200)
            .addTo(AssemblyLine);
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, new ItemStack(BlockRegister.BoxModule, 1, 2))
            .metadata(SCANNING, new Scanning(12000, TierEU.RECIPE_LV))
            .itemOutputs(new ItemStack(BlockRegister.BoxModuleUpgrad, 1, 2))
            .itemInputs(
                new ItemStack(BlockRegister.BoxRing, 8),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 998),
                GTModHandler.getModItem(GTPlusPlus.ID, "gtplusplus.blockspecialcasings.2", 64, 3),
                GTModHandler.getModItem(GTPlusPlus.ID, "gtplusplus.blocktieredcasings.1", 16, 9),
                GTModHandler.getModItem(GalacticraftCore.ID, "item.buggy", 1))
            .fluidInputs(
                FluidRegistry.getFluidStack("refinedglue", 8000),
                FluidRegistry.getFluidStack("fluid.rocketfuelmixa", 128000))
            .eut(TierEU.RECIPE_UV)
            .duration(1200)
            .addTo(AssemblyLine);
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, new ItemStack(BlockRegister.BoxModule, 1, 3))
            .metadata(SCANNING, new Scanning(12000, TierEU.RECIPE_LV))
            .itemOutputs(new ItemStack(BlockRegister.BoxModuleUpgrad, 1, 3))
            .itemInputs(
                new ItemStack(BlockRegister.BoxRing, 8),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 828),
                GTModHandler.getModItem(GTPlusPlus.ID, "gtplusplus.blockcasings.2", 32, 9),
                GTModHandler.getModItem(GTPlusPlus.ID, "MU-metaitem.01", 4, 32105),
                GTModHandler.getModItem(GTPlusPlus.ID, "itemDustRadioactiveMineralMix", 1))
            .fluidInputs(
                FluidRegistry.getFluidStack("refinedglue", 8000),
                FluidRegistry.getFluidStack("plasma.hydrogen", 12800))
            .eut(TierEU.RECIPE_UV)
            .duration(1200)
            .addTo(AssemblyLine);
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(BlockRegister.BoxModule, 1, 4),
            256000,
            512,
            8000000,
            16,
            new ItemStack[] { GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 356),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 12735),
                GTModHandler.getModItem(BartWorks.ID, "gt.bwMetaGeneratedItem0", 64, 3),
                GTOreDictUnificator.get("gemExquisitePrasiolite", 64),
                GTModHandler.getModItem(GregTech.ID, "gt.blockcasings8", 8, 14),
                GTModHandler.getModItem(GregTech.ID, "gt.blockcasings8", 16, 10),
                GTModHandler.getModItem(GregTech.ID, "gt.blockcasings8", 32, 12),
                GTModHandler.getModItem(GregTech.ID, "gt.blockcasings8", 64, 13),
                ItemList.Circuit_Parts_ResistorXSMD.get(64), ItemList.Circuit_Parts_CapacitorXSMD.get(64),
                ItemList.Circuit_Parts_DiodeXSMD.get(64), ItemList.Circuit_Parts_TransistorXSMD.get(64),
                GTModHandler.getModItem(GregTech.ID, "gt.metaitem.03", 64, 32728),
                GTModHandler.getModItem(GregTech.ID, "gt.metaitem.03", 64, 32107),
                GTModHandler.getModItem(GregTech.ID, "gt.metaitem.03", 64, 32105),
                new ItemStack(BlockRegister.BoxRing2, 16) },
            new FluidStack[] { FluidRegistry.getFluidStack("molten.ethylcyanoacrylatesuperglue", 128000),
                FluidRegistry.getFluidStack("supercoolant", 320000) },
            new ItemStack(BlockRegister.BoxModuleUpgrad, 1, 4),
            2000,
            (int) TierEU.RECIPE_UIV);
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(BlockRegister.BoxModule, 1, 5),
            128000,
            256,
            8000000,
            4,
            new ItemStack[] { GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 965),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 975),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 8, 32022),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 8, 32023),
                GTModHandler.getModItem(GTPlusPlus.ID, "gtplusplus.blockcasings.3", 64, 13),
                GTModHandler.getModItem(GTPlusPlus.ID, "gtplusplus.blockcasings.6", 64, 1),
                GTModHandler.getModItem(GoodGenerator.ID, "compactFusionCoil", 24, 2),
                GTModHandler.getModItem(GoodGenerator.ID, "compactFusionCoil", 24, 4),
                GTOreDictUnificator.get("batteryMAX", 1), new ItemStack(BlockRegister.BoxRing2, 16) },
            new FluidStack[] { FluidRegistry.getFluidStack("molten.ethylcyanoacrylatesuperglue", 128000),
                FluidRegistry.getFluidStack("molten.metastable oganesson", 64000),
                FluidRegistry.getFluidStack("plasma.radon", 288000) },
            new ItemStack(BlockRegister.BoxModuleUpgrad, 1, 5),
            2000,
            (int) TierEU.RECIPE_UEV);
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(BlockRegister.BoxModule, 1, 6),
            64000,
            128,
            8000000,
            1,
            new Object[] { GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 1132),
                ItemList.Electric_Motor_UEV.get(32), ItemList.Electric_Piston_UEV.get(8),
                ItemList.Electric_Pump_UEV.get(16), ItemList.Conveyor_Module_UEV.get(8), ItemList.Robot_Arm_UEV.get(8),
                new Object[] { OrePrefixes.circuit.get(Materials.UIV), 4 },
                GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemExtremeStorageCell.Singularity", 1),
                GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.Titanium, 32),
                GTOreDictUnificator.get("rotorAstralTitanium", 16), new ItemStack(BlockRegister.BoxRing2, 16), },
            new FluidStack[] { FluidRegistry.getFluidStack("lubricant", 128000),
                FluidRegistry.getFluidStack("grade7purifiedwater", 256000) },
            new ItemStack(BlockRegister.BoxModuleUpgrad, 1, 6),
            2000,
            (int) TierEU.RECIPE_UEV);
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(BlockRegister.BoxModule, 1, 7),
            10240,
            16,
            2000000,
            1,
            new ItemStack[] { GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 10501),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 12712),
                GTOreDictUnificator.get("blockGlassUEV", 64), GTOreDictUnificator.get("slabWood", 1),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 12718),
                GTModHandler.getModItem(UniversalSingularities.ID, "universal.general.singularity", 4, 26),
                new ItemStack(BlockRegister.BoxRing2, 16), },
            new FluidStack[] { FluidRegistry.getFluidStack("molten.ethylcyanoacrylatesuperglue", 128000),
                FluidRegistry.getFluidStack("grade6purifiedwater", 256000),
                FluidRegistry.getFluidStack("xenoxene", 128000),
                FluidRegistry.getFluidStack("bacterialsludge", 24000) },
            new ItemStack(BlockRegister.BoxModuleUpgrad, 1, 7),
            2000,
            (int) TierEU.RECIPE_UHV);
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(BlockRegister.BoxModule, 1, 8),
            81920000,
            32768,
            (int) TierEU.RECIPE_UMV,
            16,
            new ItemStack[] { GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 357),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 1006),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 1004),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 1, 15310),
                GTOreDictUnificator.get("naniteWhiteDwarfMatter", 1),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 581), ItemList.Casing_Coil_Eternal.get(64),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 3, 15470),
                GTModHandler.getModItem(TecTech.ID, "gt.blockcasingsBA0", 32, 10),
                GTModHandler.getModItem(TecTech.ID, "gt.blockcasingsBA0", 32, 11),
                GTModHandler.getModItem(TecTech.ID, "gt.stabilisation_field_generator", 1),
                new ItemStack(BlockRegister.BoxRing3, 32), },
            new FluidStack[] { FluidRegistry.getFluidStack("molten.ethylcyanoacrylatesuperglue", 10240000),
                FluidRegistry.getFluidStack("primordialmatter", 8000), FluidRegistry.getFluidStack("exciteddtsc", 8000),
                FluidRegistry.getFluidStack("molten.spacetime", 24000) },
            new ItemStack(BlockRegister.BoxModuleUpgrad, 1, 8),
            2000,
            (int) TierEU.RECIPE_UXV);
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(BlockRegister.BoxModule, 1, 9),
            10240000,
            6144,
            (int) TierEU.RECIPE_UIV,
            16,
            new ItemStack[] { GTModHandler.getModItem(GoodGenerator.ID, "componentAssemblylineCasing", 16, 10),
                GTModHandler.getModItem(GoodGenerator.ID, "componentAssemblylineCasing", 16, 10),
                GTOreDictUnificator.get("blockSpaceTime", 4), GTOreDictUnificator.get("blockSpaceTime", 8),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 32026),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 12734),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 16, 14006),
                GTOreDictUnificator.get("blockSpaceTime", 16),
                GTModHandler.getModItem(AppliedEnergistics2.ID, "tile.BlockTinyTNT", 1),
                GTModHandler.getModItem(AppliedEnergistics2.ID, "tile.BlockTinyTNT", 1),
                GTOreDictUnificator.get("blockSpaceTime", 4), GTOreDictUnificator.get("blockSpaceTime", 8),
                new ItemStack(BlockRegister.BoxRing3, 32), },
            new FluidStack[] { FluidRegistry.getFluidStack("molten.ethylcyanoacrylatesuperglue", 10240000),
                FluidRegistry.getFluidStack("glyceryl", 128000),
                FluidRegistry.getFluidStack("molten.mutatedlivingsolder", 256000),
                FluidRegistry.getFluidStack("lubricant", 256000) },
            new ItemStack(BlockRegister.BoxModuleUpgrad, 1, 9),
            2000,
            (int) TierEU.RECIPE_UIV);
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(BlockRegister.BoxModule, 1, 10),
            20480000,
            10240,
            (int) TierEU.RECIPE_UMV,
            16,
            new ItemStack[] { GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 31151),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 16999),
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 799),
                GTModHandler.getModItem(GTPlusPlus.ID, "gtplusplus.blockcasings.5", 64, 14),
                GTModHandler.getModItem(GTPlusPlus.ID, "gtplusplus.blockcasings.5", 64, 10),
                GTModHandler.getModItem(GTPlusPlus.ID, "gtplusplus.blockcasings.4", 64, 4),
                GTModHandler.getModItem(GoodGenerator.ID, "FRF_Coil_3", 64), GTOreDictUnificator.get("lensOrundum", 1),
                GTOreDictUnificator.get("rotorExtremelyUnstableNaquadah", 16), ItemList.Timepiece.get(1),
                ItemList.NaquadriaSupersolid.get(1), ItemList.SuperconductorComposite.get(1),
                ItemList.StableAdhesive.get(1), new ItemStack(BlockRegister.BoxRing3, 32), },
            new FluidStack[] { FluidRegistry.getFluidStack("molten.ethylcyanoacrylatesuperglue", 10240000),
                FluidRegistry.getFluidStack("naquadah based liquid fuel mkv (depleted)", 128000),
                FluidRegistry.getFluidStack("molten.eternity", 256000),
                FluidRegistry.getFluidStack("temporalfluid", 256000) },
            new ItemStack(BlockRegister.BoxModuleUpgrad, 1, 10),
            2000,
            (int) TierEU.RECIPE_UXV);
        CraftingManager.getInstance()
            .addRecipe(
                new ItemStack(BlockRegister.BoxModuleUpgrad, 1, 12),
                "ABA",
                "FCF",
                "DED",
                'A',
                ItemList.DysonSwarmModule.get(1),
                'B',
                GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 64, 14001),
                'C',
                GTModHandler.getModItem(GalacticraftMars.ID, "tile.marsMachine", 1, 4),
                'F',
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.StargateShieldingFoil", 1),
                'E',
                new ItemStack(BlockRegister.BoxModule, 1, 10),
                'D',
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "item.HeavyDutyRocketEngineTier4", 1));
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(BlockRegister.BoxModule, 1, 13),
            (int) TierEU.UXV,
            32767,
            (int) TierEU.UXV,
            64,
            new ItemStack[] { new ItemStack(BlockRegister.BoxRing3, 64), new ItemStack(BlockRegister.BoxRing3, 64),
                ItemList.Electric_Motor_UXV.get(64), ItemList.Electric_Pump_UXV.get(64), ItemList.Emitter_UXV.get(64),
                ItemList.Electric_Piston_UXV.get(64), ItemList.Sensor_UXV.get(64), ItemList.Robot_Arm_UXV.get(64),
                ItemList.GigaChad.get(1),
                GTModHandler.getModItem(TecTech.ID, "gt.stabilisation_field_generator", 48, 8),
                GTOreDictUnificator.get("batteryERV", 16), GTOreDictUnificator.get("ingotTitanium", 1),
                GTOreDictUnificator.get("ingotSilver", 1) },
            new FluidStack[] {
                FluidRegistry.getFluidStack("molten.magnetohydrodynamicallyconstrainedstarmatter", 20000000),
                FluidRegistry.getFluidStack("molten.shirabon", 20000000),
                FluidRegistry.getFluidStack("molten.titanium", 20000000),
                FluidRegistry.getFluidStack("molten.silver", 20000000) },
            new ItemStack(BlockRegister.BoxModuleUpgrad, 1, 13),
            Integer.MAX_VALUE - 1,
            Integer.MAX_VALUE - 1);
        // This is the debug test module recipe, maybe oneday we can re-add it...
        /*
         * TTRecipeAdder.addResearchableAssemblylineRecipe(
         * GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 1, 1145),
         * Integer.MAX_VALUE - 1,
         * 32767,
         * Integer.MAX_VALUE - 1,
         * 96,
         * new ItemStack[]{
         * GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 4, 1145),
         * ItemList.Electric_Motor_UXV.get(64),
         * ItemList.Electric_Pump_UXV.get(64),
         * ItemList.Emitter_UXV.get(64),
         * ItemList.Electric_Piston_UXV.get(64),
         * ItemList.Sensor_UXV.get(64),
         * ItemList.Robot_Arm_UXV.get(64),
         * ItemList.GigaChad.get(1),
         * GTModHandler.getModItem(SGCraft.ID, "stargateRing", 8, 0),
         * GTModHandler.getModItem(SGCraft.ID, "stargateRing", 4, 1),
         * GTModHandler.getModItem(SGCraft.ID, "stargateBase", 1),
         * GTOreDictUnificator.get("batteryERV", 64),
         * new ItemStack(BlockRegister.BoxRing3, 64),
         * new ItemStack(BlockRegister.BoxRing3, 64),
         * GTOreDictUnificator.get("ingotTitanium", 1),
         * GTOreDictUnificator.get("ingotSilver", 1)
         * },
         * new FluidStack[]{
         * FluidRegistry.getFluidStack("molten.magnetohydrodynamicallyconstrainedstarmatter", 2000000000),
         * FluidRegistry.getFluidStack("molten.shirabon", 2000000000),
         * FluidRegistry.getFluidStack("molten.titanium", 2000000000),
         * FluidRegistry.getFluidStack("molten.silver", 2000000000)},
         * new ItemStack(BlockRegister.BoxModuleUpgrad, 1, 13),
         * Integer.MAX_VALUE - 1,
         * Integer.MAX_VALUE - 1);
         */
    }

    public static void addModuleResearchRecipe() {
        // AMD 晶圆厂技术资料（基础）
        GTValues.RA.stdBuilder()
            .itemOutputs(new ItemStack(ItemRegister.bmResearchItem, 1, 0))
            .itemInputs(GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 1, 21076))
            .eut(TierEU.RECIPE_LuV)
            .duration(1200)
            .addTo(assemblerRecipes);
        // 液位调节器实验模型
        GTValues.RA.stdBuilder()
            .itemOutputs(new ItemStack(ItemRegister.bmResearchItem, 1, 1))
            .itemInputs(GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 1, 31077))
            .eut(TierEU.RECIPE_UV)
            .duration(1200)
            .addTo(assemblerRecipes);
        // 固态物质重塑理论研究
        GTValues.RA.stdBuilder()
            .itemOutputs(new ItemStack(ItemRegister.bmResearchItem, 1, 2))
            .itemInputs(GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 1, 992))
            .eut(TierEU.RECIPE_UV)
            .duration(1200)
            .addTo(assemblerRecipes);
        // 抽水马桶设计图纸（初版）
        GTValues.RA.stdBuilder()
            .itemOutputs(new ItemStack(ItemRegister.bmResearchItem, 1, 3))
            .itemInputs(GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 1, 850))
            .eut(TierEU.RECIPE_UV)
            .duration(1200)
            .addTo(assemblerRecipes);
        // 温差产生原理实验装置
        GTValues.RA.stdBuilder()
            .itemOutputs(new ItemStack(ItemRegister.bmResearchItem, 1, 4))
            .itemInputs(GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 1, 12730))
            .eut(TierEU.RECIPE_UEV)
            .duration(1200)
            .addTo(assemblerRecipes);
        // 超结构装配理论框架
        GTValues.RA.stdBuilder()
            .itemOutputs(new ItemStack(ItemRegister.bmResearchItem, 1, 5))
            .itemInputs(GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 1, 17001))
            .eut(TierEU.RECIPE_UEV)
            .duration(1200)
            .addTo(assemblerRecipes);
        // 物态并联原理研究笔记
        GTValues.RA.stdBuilder()
            .itemOutputs(new ItemStack(ItemRegister.bmResearchItem, 1, 6))
            .itemInputs(GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 1, 31150))
            .eut(TierEU.RECIPE_UEV)
            .duration(1200)
            .addTo(assemblerRecipes);
        // 聚焦技术实验报告
        GTValues.RA.stdBuilder()
            .itemOutputs(new ItemStack(ItemRegister.bmResearchItem, 1, 7))
            .itemInputs(GTModHandler.getModItem(GregTech.ID, "gt.blockmachines", 1, 15472))
            .eut(TierEU.RECIPE_UEV)
            .duration(1200)
            .addTo(assemblerRecipes);
        // 深蓝物质研究样本
        GTValues.RA.stdBuilder()
            .itemOutputs(new ItemStack(ItemRegister.bmResearchItem, 1, 8))
            .itemInputs(GTModHandler.getModItem(AppliedEnergistics2.ID, "tile.BlockAdvancedCraftingUnit", 1, 3))
            .eut(TierEU.RECIPE_UIV)
            .duration(1200)
            .addTo(assemblerRecipes);
    }
}
