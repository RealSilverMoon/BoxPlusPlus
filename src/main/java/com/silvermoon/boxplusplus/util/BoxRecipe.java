package com.silvermoon.boxplusplus.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.silvermoon.boxplusplus.util.Util.loadBoxItemFromNBT;
import static com.silvermoon.boxplusplus.util.Util.writeBoxItemToNBT;

public class BoxRecipe {
    public List<ItemStack> FinalItemInput = new ArrayList<>();
    public List<ItemStack> FinalItemOutput = new ArrayList<>();
    public List<FluidStack> FinalFluidInput = new ArrayList<>();
    public List<FluidStack> FinalFluidOutput = new ArrayList<>();
    public HashMap<Integer,Integer> requireModules = new HashMap<>();
    public int FinalTime = 0;
    public Long parallel = 0L;
    public Long FinalVoteage = 0L;
    public boolean islocked =false;

    public BoxRecipe() {
    }

    public BoxRecipe(NBTTagCompound nbt) {
        int i = 0;
        while (nbt.hasKey("InputItem" + (i + 1))) {
            FinalItemInput.add(loadBoxItemFromNBT(nbt.getCompoundTag("InputItem" + (i + 1))));
            i++;
        }
        i = 0;
        while (nbt.hasKey("OutputItem" + (i + 1))) {
            FinalItemOutput.add(loadBoxItemFromNBT(nbt.getCompoundTag("OutputItem" + (i + 1))));
            i++;
        }
        i = 0;
        while (nbt.hasKey("InputFluid" + (i + 1))) {
            FinalFluidInput.add(FluidStack.loadFluidStackFromNBT(nbt.getCompoundTag("InputFluid" + (i + 1))));
            i++;
        }
        i = 0;
        while (nbt.hasKey("OutputFluid" + (i + 1))) {
            FinalFluidOutput.add(FluidStack.loadFluidStackFromNBT(nbt.getCompoundTag("OutputFluid" + (i + 1))));
            i++;
        }
        NBTTagCompound requireModule=nbt.getCompoundTag("requireModule");
        for(i=0;i<15;i++){
            if(requireModule.hasKey(String.valueOf(i))) requireModules.put(i,requireModule.getInteger(String.valueOf(i)));
        }
        FinalTime = nbt.getInteger("Time");
        FinalVoteage= nbt.getLong("Voteage");
        islocked = nbt.getBoolean("islocked");
        parallel = nbt.getLong("parallel");
    }

    public static void ItemOneBox(List<ItemStack> input, List<ItemStack> output){
        for (ItemStack iItem : input) {
            for (ItemStack oItem : output) {
                if (Objects.equals(oItem.getUnlocalizedName(), iItem.getUnlocalizedName())) {
                    if (iItem.stackSize == oItem.stackSize) {
                        iItem.stackSize=0;
                        oItem.stackSize=0;
                        continue;
                    }
                    if (iItem.stackSize > oItem.stackSize) {
                        iItem.stackSize -= oItem.stackSize;
                        oItem.stackSize=0;
                    } else {
                        oItem.stackSize -= iItem.stackSize;
                        iItem.stackSize=0;
                    }
                }
            }
        }
        input.removeIf(item -> item.stackSize == 0);
        output.removeIf(item -> item.stackSize == 0);
    }

    public static void FluidOneBox(List<FluidStack> input, List<FluidStack> output){
        for (FluidStack iFluid : input) {
            for (FluidStack oFluid : output) {
                if (Objects.equals(oFluid.getUnlocalizedName(), iFluid.getUnlocalizedName())) {
                    if (iFluid.amount == oFluid.amount) {
                        iFluid.amount=0;
                        oFluid.amount=0;
                        continue;
                    }
                    if (iFluid.amount > oFluid.amount) {
                        iFluid.amount -= oFluid.amount;
                        oFluid.amount=0;
                    } else {
                        oFluid.amount -= iFluid.amount;
                        iFluid.amount=0;
                    }
                }
            }
        }
        input.removeIf(fluid -> fluid.amount == 0);
        output.removeIf(fluid -> fluid.amount == 0);
    }

    public NBTTagCompound RecipeToNBT() {
        NBTTagCompound recipe = new NBTTagCompound();
        for (int i = 0; i < FinalItemInput.size(); i++)
            recipe.setTag("InputItem" + (i + 1), writeBoxItemToNBT(FinalItemInput.get(i),new NBTTagCompound()));
        for (int i = 0; i < FinalItemOutput.size(); i++)
            recipe.setTag("OutputItem" + (i + 1), writeBoxItemToNBT(FinalItemOutput.get(i),new NBTTagCompound()));
        for (int i = 0; i < FinalFluidInput.size(); i++)
            recipe.setTag("InputFluid" + (i + 1), FinalFluidInput.get(i).writeToNBT(new NBTTagCompound()));
        for (int i = 0; i < FinalFluidOutput.size(); i++)
            recipe.setTag("OutputFluid" + (i + 1), FinalFluidOutput.get(i).writeToNBT(new NBTTagCompound()));
        NBTTagCompound requireModule = new NBTTagCompound();
        requireModules.forEach((k,v)-> requireModule.setInteger(String.valueOf(k),v));
        recipe.setLong("Voteage",FinalVoteage);
        recipe.setTag("requireModule",requireModule);
        recipe.setInteger("Time", FinalTime);
        recipe.setBoolean("islocked", islocked);
        recipe.setLong("parallel", parallel);
        return recipe;
    }

    public int calHeight() {
        return FinalItemInput.size() + FinalFluidInput.size() + FinalItemOutput.size() + FinalFluidOutput.size();
    }
}
