package com.silvermoon.boxplusplus.util;

import static com.silvermoon.boxplusplus.util.Util.loadBoxItemFromNBT;
import static com.silvermoon.boxplusplus.util.Util.writeBoxItemToNBT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.FluidStack;

import com.glodblock.github.common.item.ItemFluidDrop;

import appeng.api.storage.data.IAEItemStack;
import appeng.util.item.AEItemStack;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class BoxRecipe {

    public List<ItemStack> FinalItemInput = new ArrayList<>();
    public List<ItemStack> FinalItemOutput = new ArrayList<>();
    public List<FluidStack> FinalFluidInput = new ArrayList<>();
    public List<FluidStack> FinalFluidOutput = new ArrayList<>();
    public HashMap<Integer, Integer> requireModules = new HashMap<>();
    public int FinalTime = 0;
    public Long parallel = 0L;
    public Long FinalVoteage = 0L;
    public boolean islocked = false;

    public BoxRecipe() {}

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
        NBTTagCompound requireModule = nbt.getCompoundTag("requireModule");
        for (i = 0; i < 15; i++) {
            if (requireModule.hasKey(String.valueOf(i)))
                requireModules.put(i, requireModule.getInteger(String.valueOf(i)));
        }
        FinalTime = nbt.getInteger("Time");
        FinalVoteage = nbt.getLong("Voteage");
        islocked = nbt.getBoolean("islocked");
        parallel = nbt.getLong("parallel");
    }

    public static void ItemOnBox(List<ItemStack> input, List<ItemStack> output) {
        for (ItemStack iItem : input) {
            for (ItemStack oItem : output) {
                if (GT_Utility.areStacksEqual(oItem, iItem, true) || (oItem.getUnlocalizedName()
                    .startsWith("item.Circuit") && GT_OreDictUnificator.isInputStackEqual(
                    iItem,
                    GT_OreDictUnificator.get(oItem)))) {
                    if (iItem.stackSize == oItem.stackSize) {
                        iItem.stackSize = 0;
                        oItem.stackSize = 0;
                        continue;
                    }
                    if (iItem.stackSize > oItem.stackSize) {
                        iItem.stackSize -= oItem.stackSize;
                        oItem.stackSize = 0;
                    } else {
                        oItem.stackSize -= iItem.stackSize;
                        iItem.stackSize = 0;
                    }
                }
            }
        }
        input.removeIf(item -> item.stackSize == 0);
        output.removeIf(item -> item.stackSize == 0);
    }

    public static void FluidOnBox(List<FluidStack> input, List<FluidStack> output) {
        for (FluidStack iFluid : input) {
            for (FluidStack oFluid : output) {
                if (Objects.equals(oFluid.getUnlocalizedName(), iFluid.getUnlocalizedName())) {
                    if (iFluid.amount == oFluid.amount) {
                        iFluid.amount = 0;
                        oFluid.amount = 0;
                        continue;
                    }
                    if (iFluid.amount > oFluid.amount) {
                        iFluid.amount -= oFluid.amount;
                        oFluid.amount = 0;
                    } else {
                        oFluid.amount -= iFluid.amount;
                        iFluid.amount = 0;
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
            recipe.setTag("InputItem" + (i + 1), writeBoxItemToNBT(FinalItemInput.get(i), new NBTTagCompound()));
        for (int i = 0; i < FinalItemOutput.size(); i++)
            recipe.setTag("OutputItem" + (i + 1), writeBoxItemToNBT(FinalItemOutput.get(i), new NBTTagCompound()));
        for (int i = 0; i < FinalFluidInput.size(); i++)
            recipe.setTag("InputFluid" + (i + 1),
                FinalFluidInput.get(i)
                    .writeToNBT(new NBTTagCompound()));
        for (int i = 0; i < FinalFluidOutput.size(); i++)
            recipe.setTag("OutputFluid" + (i + 1),
                FinalFluidOutput.get(i)
                    .writeToNBT(new NBTTagCompound()));
        NBTTagCompound requireModule = new NBTTagCompound();
        requireModules.forEach((k, v) -> requireModule.setInteger(String.valueOf(k), v));
        recipe.setLong("Voteage", FinalVoteage);
        recipe.setTag("requireModule", requireModule);
        recipe.setInteger("Time", FinalTime);
        recipe.setBoolean("islocked", islocked);
        recipe.setLong("parallel", parallel);
        return recipe;
    }

    public NBTTagCompound RecipeToAE2ItemPattern(String ls) {
        final NBTTagCompound encodedValue = new NBTTagCompound();
        final NBTTagList tagIn = new NBTTagList();
        final NBTTagList tagOut = new NBTTagList();
        for (final ItemStack i : this.FinalItemInput) {
            tagIn.appendTag(Util.createItemTag(i));
        }
        if (ls.equals("")) {
            for (final ItemStack i : this.FinalItemOutput) {
                tagOut.appendTag(Util.createItemTag(i));
            }
        } else {
            String[] var1 = ls.split(",");
            for (String s : var1) {
                if (s.contains("-")) {
                    String[] var2 = s.split("-");
                    int start = Integer.parseInt(var2[0]);
                    int end = Integer.parseInt(var2[1]);
                    for (int i = start - 1; i < end; i++) {
                        tagOut.appendTag(Util.createItemTag(FinalItemOutput.get(i)));
                    }
                } else tagOut.appendTag(Util.createItemTag(FinalItemOutput.get(Integer.parseInt(s) - 1)));
            }
        }
        encodedValue.setTag("in", tagIn);
        encodedValue.setTag("out", tagOut);
        encodedValue.setBoolean("crafting", false);
        encodedValue.setBoolean("substitute", false);
        encodedValue.setBoolean("beSubstitute", false);
        return encodedValue;
    }

    public int calHeight() {
        return FinalItemInput.size() + FinalFluidInput.size() + FinalItemOutput.size() + FinalFluidOutput.size();
    }

    public IAEItemStack[] transInputsToAE2Stuff() {
        IAEItemStack[] stacks = new IAEItemStack[FinalItemInput.size() + FinalFluidInput.size()];
        int i = 0;
        for (ItemStack item : FinalItemInput) {
            stacks[i] = AEItemStack.create(item);
            i++;
        }
        for (FluidStack fluid : FinalFluidInput) {
            stacks[i] = ItemFluidDrop.newAeStack(fluid);
            i++;
        }
        return stacks;
    }

    public IAEItemStack[] transOutputsToAE2Stuff(String itemKey, String fluidKey) {
        List<IAEItemStack> stacks = new ArrayList<>();
        if (itemKey.equals("")) {
            for (ItemStack item : FinalItemOutput) {
                stacks.add(AEItemStack.create(item));
            }
        } else if (!itemKey.equals("0")) {
            String[] var1 = itemKey.split(",");
            for (String s : var1) {
                if (s.contains("-")) {
                    String[] var2 = s.split("-");
                    int start = Integer.parseInt(var2[0]);
                    int end = Integer.parseInt(var2[1]);
                    for (int i = start - 1; i < end; i++) {
                        stacks.add(AEItemStack.create(FinalItemOutput.get(i)));
                    }
                } else stacks.add(AEItemStack.create(FinalItemOutput.get(Integer.parseInt(s) - 1)));
            }
        }
        if (fluidKey.equals("")) {
            for (FluidStack fluid : FinalFluidOutput) {
                stacks.add(ItemFluidDrop.newAeStack(fluid));
            }
        } else if (!fluidKey.equals("0")) {
            String[] var1 = fluidKey.split(",");
            for (String s : var1) {
                if (s.contains("-")) {
                    String[] var2 = s.split("-");
                    int start = Integer.parseInt(var2[0]);
                    int end = Integer.parseInt(var2[1]);
                    for (int i = start - 1; i < end; i++) {
                        stacks.add(ItemFluidDrop.newAeStack(FinalFluidOutput.get(i)));
                    }
                } else stacks.add(ItemFluidDrop.newAeStack(FinalFluidOutput.get(Integer.parseInt(s) - 1)));
            }
        }
        return stacks.toArray(new IAEItemStack[0]);
    }
}
