package com.silvermoon.boxplusplus.common.loader;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_ModHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;

import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.*;
import static gregtech.api.util.GT_RecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GT_RecipeConstants.RESEARCH_TIME;

public class RecipeLoader {
    //todo: Add recipes
    public void init(){
        addBoxRecipe();
        addMachineBlockRecipe();
    }
    public void addBoxRecipe(){
        GT_Values.RA.stdBuilder().metadata(RESEARCH_ITEM, ItemList.Machine_Multi_Assemblyline.get(1))
            .metadata(RESEARCH_TIME, 24000)
            .itemOutputs(TileEntitiesLoader.Box.getStackForm(1))
            .noFluidOutputs()
            .itemInputs(
                new ItemStack(BlockRegister.BoxRing,1,0),
                GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial",4,59),
                GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial",8,58),
                GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial",16,57),
                GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial",32,38),
                GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial",64,37),
                new ItemStack(BlockRegister.SpaceExtend,64,0),
                ItemList.Field_Generator_IV.get(4),
                GT_ModHandler.getModItem(GregTech.ID, "gt.blockmachines",4,15470),
                ItemList.Tool_DataOrb.get(4),
                ItemList.Casing_Pipe_Titanium.get(64)
            )
            .fluidInputs(
                FluidRegistry.getFluidStack("molten.titanium",16416),
                FluidRegistry.getFluidStack("lubricant",16000),
                FluidRegistry.getFluidStack("ic2uumater",4000)
            )
            .eut(TierEU.RECIPE_LuV)
            .duration(24000)
            .addTo(sAssemblylineVisualRecipes);
    }
    public void addMachineBlockRecipe(){

    }
}
