package com.silvermoon.boxplusplus.util;

import static com.silvermoon.boxplusplus.util.Util.*;
import static gregtech.common.blocks.GT_Item_Machines.getMetaTileEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.github.bartimaeusnek.bartworks.API.recipe.BartWorksRecipeMaps;
import com.silvermoon.boxplusplus.api.IBoxable;
import com.silvermoon.boxplusplus.boxplusplus;
import com.silvermoon.boxplusplus.common.tileentities.GTMachineBox;
import com.silvermoon.boxplusplus.network.MessageRouting;
import com.silvermoon.boxplusplus.network.NetworkLoader;

import appeng.container.ContainerNull;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.RecipeCatalysts;
import fox.spiteful.avaritia.crafting.ExtremeShapedOreRecipe;
import fox.spiteful.avaritia.crafting.ExtremeShapedRecipe;
import goodgenerator.api.recipe.GoodGeneratorRecipeMaps;
import gregtech.api.enums.*;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.*;
import gregtech.common.items.behaviors.Behaviour_DataOrb;
import gregtech.nei.GT_NEI_DefaultHandler;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class BoxRoutings {

    public ItemStack RoutingMachine;
    public List<ItemStack> InputItem = new ArrayList<>();
    public List<ItemStack> OutputItem = new ArrayList<>();
    public List<Integer> OutputChance = new ArrayList<>();
    public List<FluidStack> InputFluid = new ArrayList<>();
    public List<FluidStack> OutputFluid = new ArrayList<>();
    public int special = 0;
    public int Parallel = 1;
    public int time;
    public Long voltage;

    public BoxRoutings(GT_Recipe recipe, ItemStack machine) {
        InputItem.addAll(Arrays.asList(recipe.mInputs));
        InputItem.removeAll(Collections.singleton(null));
        OutputItem.addAll(Arrays.asList(recipe.mOutputs));
        if (machine.getUnlocalizedName()
            .contains("multimachine.plasmaforge")) {
            for (int i = 0; i < OutputItem.size(); i++) OutputChance.add(7500);
        } else for (int i = 0; i < OutputItem.size(); i++) OutputChance.add(recipe.getOutputChance(i));
        OutputItem.removeAll(Collections.singleton(null));
        InputFluid.addAll(Arrays.asList(recipe.mFluidInputs));
        OutputFluid.addAll(Arrays.asList(recipe.mFluidOutputs));
        RoutingMachine = machine;
        voltage = (long) recipe.mEUt;
        time = recipe.mDuration;
        // do some special service
        switch (machine.getUnlocalizedName()
            .substring(17)) {
            case "multimachine.plasmaforge" -> {
                time *= 4;
                OutputFluid.forEach(f -> f.amount = (int) (f.amount * 0.75));
            }
            case "multimachine.blastfurnace", "multimachine.adv.blastfurnace", "megablastfurnace" -> this.special = recipe.mSpecialValue;
            case "componentassemblyline" -> time /= 16;
            case "quantumforcetransformer.controller.tier.single" -> OutputFluid
                .forEach(f -> f.amount = f.amount / (OutputFluid.size() + OutputItem.size()));
        }
    }

    public BoxRoutings(ItemStack inputs, ItemStack outputs, ItemStack machine, Long v, int t) {
        InputItem.add(inputs);
        OutputItem.add(outputs);
        OutputChance.add(10000);
        RoutingMachine = machine;
        voltage = v;
        time = t;
    }

    public BoxRoutings(ItemStack[] inputs, ItemStack outputs, FluidStack[] fInputs, ItemStack machine, Long v, int t) {
        InputItem.addAll(Arrays.asList(inputs));
        InputFluid.addAll(Arrays.asList(fInputs));
        OutputItem.add(outputs);
        OutputChance.add(10000);
        RoutingMachine = machine;
        voltage = v;
        time = t;
    }

    public BoxRoutings(FluidStack fOutputs, ItemStack machine, Long v, int t) {
        OutputFluid.add(fOutputs);
        RoutingMachine = machine;
        voltage = v;
        time = t;
    }

    public BoxRoutings(InventoryCrafting inputs, ItemStack outputs, ItemStack machine) {
        for (int i = 0; i < 9; i++) {
            if (inputs.getStackInSlot(i) == null) continue;
            InputItem.add(
                inputs.getStackInSlot(i)
                    .copy());
        }
        ItemStack b = outputs.copy();
        OutputItem.add(b);
        OutputChance.add(10000);
        RoutingMachine = machine;
        voltage = 120L;
        time = 100;
    }

    public BoxRoutings(NBTTagCompound routingNBT) {
        RoutingMachine = ItemStack.loadItemStackFromNBT(routingNBT.getCompoundTag("machine"));
        int i = 0;
        while (routingNBT.hasKey("InputItem" + (i + 1))) {
            InputItem.add(loadBoxItemFromNBT(routingNBT.getCompoundTag("InputItem" + (i + 1))));
            i++;
        }
        i = 0;
        while (routingNBT.hasKey("OutputItem" + (i + 1))) {
            OutputItem.add(loadBoxItemFromNBT(routingNBT.getCompoundTag("OutputItem" + (i + 1))));
            OutputChance.add(routingNBT.getInteger("OutputChance" + (i + 1)));
            i++;
        }
        i = 0;
        while (routingNBT.hasKey("InputFluid" + (i + 1))) {
            InputFluid.add(FluidStack.loadFluidStackFromNBT(routingNBT.getCompoundTag("InputFluid" + (i + 1))));
            i++;
        }
        i = 0;
        while (routingNBT.hasKey("OutputFluid" + (i + 1))) {
            OutputFluid.add(FluidStack.loadFluidStackFromNBT(routingNBT.getCompoundTag("OutputFluid" + (i + 1))));
            i++;
        }
        voltage = routingNBT.getLong("Voltage");
        Parallel = routingNBT.getInteger("Parallel");
        time = routingNBT.getInteger("Time");
        special = routingNBT.getInteger("Sp");
    }

    public BoxRoutings(NBTTagCompound routingNBT, boolean isUNBT) {
        RoutingMachine = readBoxItemFromUNBT(routingNBT.getCompoundTag("machine"));
        int i = 0;
        while (routingNBT.hasKey("InputItem" + (i + 1))) {
            InputItem.add(readBoxItemFromUNBT(routingNBT.getCompoundTag("InputItem" + (i + 1))));
            i++;
        }
        i = 0;
        while (routingNBT.hasKey("OutputItem" + (i + 1))) {
            OutputItem.add(readBoxItemFromUNBT(routingNBT.getCompoundTag("OutputItem" + (i + 1))));
            OutputChance.add(routingNBT.getInteger("OutputChance" + (i + 1)));
            i++;
        }
        i = 0;
        while (routingNBT.hasKey("InputFluid" + (i + 1))) {
            InputFluid.add(FluidStack.loadFluidStackFromNBT(routingNBT.getCompoundTag("InputFluid" + (i + 1))));
            i++;
        }
        i = 0;
        while (routingNBT.hasKey("OutputFluid" + (i + 1))) {
            OutputFluid.add(FluidStack.loadFluidStackFromNBT(routingNBT.getCompoundTag("OutputFluid" + (i + 1))));
            i++;
        }
        voltage = routingNBT.getLong("Voltage");
        Parallel = routingNBT.getInteger("Parallel");
        time = routingNBT.getInteger("Time");
        special = routingNBT.getInteger("Sp");
    }

    public NBTTagCompound routingToNbt() {
        NBTTagCompound routing = new NBTTagCompound();
        routing.setTag("machine", RoutingMachine.writeToNBT(new NBTTagCompound()));
        for (int i = 0; i < InputItem.size(); i++)
            routing.setTag("InputItem" + (i + 1), writeBoxItemToNBT(InputItem.get(i), new NBTTagCompound()));
        for (int i = 0; i < OutputItem.size(); i++) {
            routing.setTag("OutputItem" + (i + 1), writeBoxItemToNBT(OutputItem.get(i), new NBTTagCompound()));
            routing.setInteger("OutputChance" + (i + 1), OutputChance.get(i));
        }
        for (int i = 0; i < InputFluid.size(); i++) routing.setTag(
            "InputFluid" + (i + 1),
            InputFluid.get(i)
                .writeToNBT(new NBTTagCompound()));
        for (int i = 0; i < OutputFluid.size(); i++) routing.setTag(
            "OutputFluid" + (i + 1),
            OutputFluid.get(i)
                .writeToNBT(new NBTTagCompound()));
        routing.setLong("Voltage", voltage);
        routing.setInteger("Parallel", Parallel);
        routing.setInteger("Time", time);
        routing.setInteger("Sp", special);
        return routing;
    }

    public NBTTagCompound routingToUNbt() {
        NBTTagCompound routing = new NBTTagCompound();
        routing.setTag("machine", writeBoxItemToUNBT(RoutingMachine, new NBTTagCompound()));
        for (int i = 0; i < InputItem.size(); i++)
            routing.setTag("InputItem" + (i + 1), writeBoxItemToUNBT(InputItem.get(i), new NBTTagCompound()));
        for (int i = 0; i < OutputItem.size(); i++) {
            routing.setTag("OutputItem" + (i + 1), writeBoxItemToUNBT(OutputItem.get(i), new NBTTagCompound()));
            routing.setInteger("OutputChance" + (i + 1), OutputChance.get(i));
        }
        for (int i = 0; i < InputFluid.size(); i++) routing.setTag(
            "InputFluid" + (i + 1),
            InputFluid.get(i)
                .writeToNBT(new NBTTagCompound()));
        for (int i = 0; i < OutputFluid.size(); i++) routing.setTag(
            "OutputFluid" + (i + 1),
            OutputFluid.get(i)
                .writeToNBT(new NBTTagCompound()));
        routing.setLong("Voltage", voltage);
        routing.setInteger("Parallel", Parallel);
        routing.setInteger("Time", time);
        routing.setInteger("Sp", special);
        return routing;
    }

    public int calHeight() {
        return InputItem.size() + OutputItem.size() + InputFluid.size() + OutputFluid.size();
    }

    public static void checkRouting(GTMachineBox box) {
        if (box.mInputBusses.isEmpty()) {
            box.routingStatus = 1;
            return;
        }
        RecipeMap<?> recipeMap;
        GT_Recipe routingRecipe = null;
        List<ItemStack> allInputItems = box.getStoredInputs();
        for (GT_MetaTileEntity_Hatch_InputBus inputBus : box.mInputBusses) {
            for (int i = inputBus.getSizeInventory() - 1; i >= 0; i--) {
                if (inputBus.getStackInSlot(i) != null) {
                    {
                        // Electromagneticseparator do not have Multimachine. That sucks.
                        if (inputBus.getStackInSlot(i)
                            .getUnlocalizedName()
                            .equals("gt.blockmachines.basicmachine.electromagneticseparator.tier.06")) {
                            recipeMap = RecipeMaps.electroMagneticSeparatorRecipes;
                            routingRecipe = recipeMap.findRecipe(
                                box.getBaseMetaTileEntity(),
                                true,
                                true,
                                Long.MAX_VALUE / 10,
                                box.getStoredFluids()
                                    .toArray(new FluidStack[0]),
                                allInputItems.toArray(new ItemStack[0]));
                            if (routingRecipe != null) {
                                box.routingMap.add(new BoxRoutings(routingRecipe.copy(), inputBus.getStackInSlot(i)));
                                box.routingStatus = 0;
                            } else {
                                box.routingStatus = 3;
                            }
                            return;
                        }
                        // Really? You add neutronium compressor?
                        if (inputBus.getStackInSlot(i)
                            .getUnlocalizedName()
                            .equals("tile.neutronium_compressor")) {
                            for (ItemStack item : allInputItems) {
                                ItemStack out = fox.spiteful.avaritia.crafting.CompressorManager.getOutput(item);
                                if (out != null) {
                                    ItemStack in = item.copy();
                                    in.stackSize = fox.spiteful.avaritia.crafting.CompressorManager.getCost(item);
                                    ItemStack machine = inputBus.getStackInSlot(i)
                                        .copy();
                                    machine.stackSize = 1;
                                    box.routingMap
                                        .add(new BoxRoutings(in, out, machine, TierEU.RECIPE_ZPM, TickTime.MINUTE));
                                    box.routingStatus = 0;
                                    return;
                                }
                            }
                            box.routingStatus = 3;
                            return;
                        }
                        // Extreme Craft Table
                        if (inputBus.getStackInSlot(i)
                            .getUnlocalizedName()
                            .equals("tile.dire_crafting")) {
                            for (ItemStack item : box.getStoredInputs()) {
                                List recipeList = fox.spiteful.avaritia.crafting.ExtremeCraftingManager.getInstance()
                                    .getRecipeList();
                                for (Object recipe : recipeList) {
                                    if (recipe instanceof ExtremeShapedRecipe exRecipe) {
                                        if (GT_OreDictUnificator.isInputStackEqual(
                                            item,
                                            GT_OreDictUnificator.get(exRecipe.getRecipeOutput()))) {
                                            ItemStack[] in = exRecipe.recipeItems;
                                            ItemContainer var = new ItemContainer();
                                            for (ItemStack itemIn : in) {
                                                if (itemIn == null) continue;
                                                var.addItemStack(itemIn, 1, 10000);
                                            }
                                            ItemStack machine = inputBus.getStackInSlot(i)
                                                .copy();
                                            machine.stackSize = 1;
                                            box.routingMap.add(
                                                new BoxRoutings(
                                                    var.getItemStack()
                                                        .toArray(new ItemStack[0]),
                                                    exRecipe.getRecipeOutput(),
                                                    new FluidStack[] {},
                                                    machine,
                                                    TierEU.RECIPE_UV,
                                                    TickTime.MINUTE));
                                            box.routingStatus = 0;
                                            return;
                                        }
                                    } else if (recipe instanceof ExtremeShapedOreRecipe exRecipe) {
                                        if (GT_OreDictUnificator.isInputStackEqual(item, exRecipe.getRecipeOutput())) {
                                            Object[] in = exRecipe.getInput();
                                            ItemContainer var = new ItemContainer();
                                            for (Object ObjtecIn : in) {
                                                if (ObjtecIn == null) continue;
                                                if (ObjtecIn instanceof ItemStack itemIn)
                                                    var.addItemStack(GT_OreDictUnificator.get(itemIn), 1, 10000);
                                                if (ObjtecIn instanceof ArrayList listIn) var.addItemStack(
                                                    GT_OreDictUnificator.get((ItemStack) listIn.get(0)),
                                                    1,
                                                    10000);
                                            }
                                            ItemStack machine = inputBus.getStackInSlot(i)
                                                .copy();
                                            machine.stackSize = 1;
                                            box.routingMap.add(
                                                new BoxRoutings(
                                                    var.getItemStack()
                                                        .toArray(new ItemStack[0]),
                                                    exRecipe.getRecipeOutput(),
                                                    new FluidStack[] {},
                                                    machine,
                                                    TierEU.RECIPE_UV,
                                                    TickTime.MINUTE));
                                            box.routingStatus = 0;
                                            return;
                                        }
                                    }
                                }
                            }
                            box.routingStatus = 3;
                            return;
                        }
                    }
                    if (getMetaTileEntity(
                        inputBus.getStackInSlot(i)) instanceof GT_MetaTileEntity_MultiBlockBase RoutingMachine) {
                        boxplusplus.LOG.debug(RoutingMachine.mName);
                        List<ItemStack> ItemInputs = deepCopyItemList(box.getStoredInputs());
                        List<FluidStack> FluidInputs = deepCopyFluidList(box.getStoredFluids());
                        switch (RoutingMachine.mName) {
                            case "industrialmultimachine.controller.tier.single" -> {
                                ItemStack Circuit = findfirstCircuit(ItemInputs);
                                if (Circuit == null) {
                                    box.routingStatus = 4;
                                    return;
                                }
                                recipeMap = getMMRecipeMap(Circuit.getItemDamage());
                                ItemInputs.remove(Circuit);
                            }
                            case "multimachine.multifurnace" -> {
                                for (ItemStack input : ItemInputs) {
                                    ItemStack output = GT_OreDictUnificator.get(
                                        FurnaceRecipes.smelting()
                                            .getSmeltingResult(input));
                                    if (output != null) {
                                        ItemStack var1 = input.copy();
                                        var1.stackSize = 1;
                                        ItemStack var2 = output.copy();
                                        var2.stackSize = 1;
                                        box.routingMap
                                            .add(new BoxRoutings(var1, var2, RoutingMachine.getStackForm(1), 30L, 100));
                                        box.routingStatus = 0;
                                        return;
                                    }
                                }
                                box.routingStatus = 3;
                                return;
                            }
                            case "mxrandomlargemolecularassembler" -> {
                                InventoryCrafting fakeCraft = new InventoryCrafting(new ContainerNull(), 3, 3);
                                if (i == 1) {
                                    fakeCraft.setInventorySlotContents(0, ItemInputs.get(0));
                                } else {
                                    for (int j = 0; j < 9; j++) {
                                        fakeCraft.setInventorySlotContents(j, inputBus.getStackInSlot(j));
                                    }
                                }
                                ItemStack out = CraftingManager.getInstance()
                                    .findMatchingRecipe(
                                        fakeCraft,
                                        box.getBaseMetaTileEntity()
                                            .getWorld());
                                if (out != null) {
                                    box.routingMap.add(new BoxRoutings(fakeCraft, out, RoutingMachine.getStackForm(1)));
                                    box.routingStatus = 0;
                                    return;
                                }
                                box.routingStatus = 3;
                                return;
                            }
                            case "industrialrockcrusher.controller.tier.single" -> {
                                ItemStack Circuit = findfirstCircuit(ItemInputs);
                                if (Circuit == null) {
                                    box.routingStatus = 4;
                                    return;
                                }
                                ItemStack output;
                                switch (Circuit.getItemDamage()) {
                                    case 1 -> output = new ItemStack(
                                        Blocks.cobblestone,
                                        (int) Math.pow(16, box.ringCount));
                                    case 2 -> output = new ItemStack(Blocks.stone, (int) Math.pow(16, box.ringCount));
                                    case 3 -> output = new ItemStack(
                                        Blocks.obsidian,
                                        (int) Math.pow(16, box.ringCount));
                                    default -> {
                                        box.routingStatus = 3;
                                        return;
                                    }
                                }
                                ItemStack input = Circuit.copy();
                                input.stackSize = 0;
                                box.routingMap
                                    .add(new BoxRoutings(input, output, RoutingMachine.getStackForm(1), 30L, 20));
                                box.routingStatus = 0;
                                return;
                            }
                            case "industrialbender.controller.tier.single" -> {
                                ItemStack Circuit = findfirstCircuit(ItemInputs);
                                if (Circuit == null) {
                                    box.routingStatus = 4;
                                    return;
                                }
                                switch (Circuit.getItemDamage()) {
                                    case 1 -> recipeMap = RecipeMaps.benderRecipes;
                                    case 2 -> recipeMap = RecipeMaps.formingPressRecipes;
                                    default -> {
                                        box.routingStatus = 4;
                                        return;
                                    }
                                }
                                ItemInputs.remove(Circuit);
                            }
                            case "industrialwashplant.controller.tier.single" -> {
                                ItemStack Circuit = findfirstCircuit(ItemInputs);
                                if (Circuit == null) {
                                    box.routingStatus = 4;
                                    return;
                                }
                                switch (Circuit.getItemDamage()) {
                                    case 1 -> recipeMap = RecipeMaps.oreWasherRecipes;
                                    case 2 -> recipeMap = RecipeMaps.chemicalBathRecipes;
                                    default -> {
                                        box.routingStatus = 4;
                                        return;
                                    }
                                }
                                ItemInputs.remove(Circuit);
                            }
                            case "multimachine.assemblyline" -> {
                                ItemStack data = null;
                                for (ItemStack item : ItemInputs) {
                                    if (ItemList.Tool_DataStick.isStackEqual(item, false, true)) data = item.copy();
                                }
                                if (data == null) {
                                    box.routingStatus = 5;
                                    return;
                                }
                                // We can find assemblyline recipe using the original method, but no need to update it,
                                // nor check it
                                GT_AssemblyLineUtils.LookupResult tLookupResult = GT_AssemblyLineUtils
                                    .findAssemblyLineRecipeFromDataStick(data, false);
                                if (tLookupResult.getType() == GT_AssemblyLineUtils.LookupResultType.INVALID_STICK) {
                                    box.routingStatus = 5;
                                    return;
                                }
                                GT_Recipe.GT_Recipe_AssemblyLine tRecipe = tLookupResult.getRecipe();
                                ItemStack[] in = Arrays.copyOf(tRecipe.mInputs, tRecipe.mInputs.length);
                                for (int j = 0; j < tRecipe.mOreDictAlt.length; j++) {
                                    if (tRecipe.mOreDictAlt[j] == null) continue;
                                    in[j] = GT_OreDictUnificator.get(false, in[j]);
                                    for (ItemStack replace : ItemInputs) {
                                        if (GT_OreDictUnificator.getAssociation(replace) != null
                                            && GT_OreDictUnificator.isInputStackEqual(replace, in[j])) {
                                            in[j] = new ItemStack(
                                                replace.getItem(),
                                                in[j].stackSize,
                                                replace.getItemDamage());
                                        }
                                    }
                                }
                                box.routingMap.add(
                                    new BoxRoutings(
                                        in,
                                        tRecipe.mOutput,
                                        tRecipe.mFluidInputs,
                                        RoutingMachine.getStackForm(1),
                                        (long) tRecipe.mEUt,
                                        tRecipe.mDuration));
                                box.routingStatus = 0;
                                return;
                            }
                            case "chemicalplant.controller.tier.single" -> {
                                recipeMap = RoutingMachine.getRecipeMap();
                                if (recipeMap == null) {
                                    box.routingStatus = 3;
                                    return;
                                }
                                // The chemicalplant use tier-based recipe check method, it will be better not to change
                                // it.
                                // But not anymore.
                                routingRecipe = RoutingMachine.getRecipeMap()
                                    .findRecipe(
                                        box.getBaseMetaTileEntity(),
                                        true,
                                        Long.MAX_VALUE / 10,
                                        FluidInputs.toArray(new FluidStack[0]),
                                        ItemInputs.toArray(new ItemStack[0]));
                                if (routingRecipe == null) {
                                    box.routingStatus = 3;
                                    return;
                                }
                                routingRecipe = routingRecipe.copy();
                                for (ItemStack item : routingRecipe.mInputs) {
                                    if (ItemUtils.isCatalyst(item)) {
                                        item.stackSize = 0;
                                        break;
                                    }
                                }
                            }
                            case "circuitassemblyline" -> {
                                // Circuitassemblyline will check imprint first. Let us do the same thing here.
                                recipeMap = BartWorksRecipeMaps.circuitAssemblyLineRecipes;
                                if (inputBus.getStackInSlot(i)
                                    .getTagCompound() == null
                                    || !inputBus.getStackInSlot(i)
                                        .getTagCompound()
                                        .hasKey("Type")) {
                                    box.routingStatus = 6;
                                    return;
                                }
                                for (GT_Recipe recipe : recipeMap.getAllRecipes()) {
                                    if (GT_Utility.areStacksEqual(
                                        recipe.mOutputs[0],
                                        ItemStack.loadItemStackFromNBT(
                                            inputBus.getStackInSlot(i)
                                                .getTagCompound()
                                                .getCompoundTag("Type")),
                                        true)) {
                                        if (recipe.isRecipeInputEqual(
                                            false,
                                            true,
                                            FluidInputs.toArray(new FluidStack[0]),
                                            ItemInputs.toArray(new ItemStack[0]))) {
                                            routingRecipe = recipe;
                                            break;
                                        }
                                    }
                                }
                            }
                            case "industrialarcfurnace.controller.tier.single" -> {
                                ItemStack Circuit = findfirstCircuit(ItemInputs);
                                if (Circuit == null) {
                                    box.routingStatus = 4;
                                    return;
                                }
                                switch (Circuit.getItemDamage()) {
                                    case 1 -> recipeMap = RecipeMaps.arcFurnaceRecipes;
                                    case 2 -> recipeMap = RecipeMaps.plasmaArcFurnaceRecipes;
                                    default -> {
                                        box.routingStatus = 4;
                                        return;
                                    }
                                }
                                ItemInputs.remove(Circuit);
                            }
                            case "gtpp.multimachine.replicator" -> {
                                recipeMap = RecipeMaps.replicatorRecipes;
                                Materials replicatorItem = null;
                                for (ItemStack item : ItemInputs) {
                                    if (Behaviour_DataOrb.getDataName(item)
                                        .isEmpty()) continue;
                                    replicatorItem = Element.get(Behaviour_DataOrb.getDataName(item)).mLinkedMaterials
                                        .get(0);
                                    break;
                                }
                                if (replicatorItem == Materials._NULL) {
                                    box.routingStatus = 7;
                                    return;
                                }
                                for (GT_Recipe recipe : recipeMap.getAllRecipes()) {
                                    if (!(recipe.mSpecialItems instanceof ItemStack[]var1)) {
                                        continue;
                                    }
                                    if (replicatorItem.equals(
                                        Element.get(Behaviour_DataOrb.getDataName(var1[0])).mLinkedMaterials.get(0))) {
                                        box.routingMap.add(new BoxRoutings(recipe, RoutingMachine.getStackForm(1)));
                                        box.routingStatus = 0;
                                        return;
                                    }
                                }
                                box.routingStatus = 3;
                                return;
                            }
                            case "industrialmassfab.controller.tier.single" -> {
                                box.routingMap.add(
                                    new BoxRoutings(
                                        FluidRegistry.getFluidStack("ic2uumatter", 1000),
                                        RoutingMachine.getStackForm(1),
                                        TierEU.RECIPE_UEV,
                                        20));
                                box.routingStatus = 0;
                                return;
                            }
                            case "preciseassembler" -> recipeMap = GoodGeneratorRecipeMaps.preciseAssemblerRecipes;
                            default -> {
                                recipeMap = (RoutingMachine instanceof IBoxable boxable)
                                    ? boxable.getRealRecipeMap(RoutingMachine)
                                    : RoutingMachine.getRecipeMap();
                                if (recipeMap == null) {
                                    box.routingStatus = 3;
                                    return;
                                }
                            }
                        }
                        ItemInputs.remove(inputBus.getStackInSlot(i));
                        if (routingRecipe == null) routingRecipe = recipeMap.findRecipe(
                            box.getBaseMetaTileEntity(),
                            true,
                            true,
                            Long.MAX_VALUE / 10,
                            FluidInputs.toArray(new FluidStack[0]),
                            ItemInputs.toArray(new ItemStack[0]));
                        if (routingRecipe != null) {
                            GT_Recipe tempRecipe = routingRecipe.copy();
                            for (int j = 0; j < tempRecipe.mInputs.length; j++) {
                                if (tempRecipe.mInputs[j] == null) continue;
                                if (GT_OreDictUnificator.getAssociation(tempRecipe.mInputs[j]) != null) {
                                    for (ItemStack si : box.getStoredInputs()) {
                                        if (GT_OreDictUnificator.isInputStackEqual(
                                            tempRecipe.mInputs[j],
                                            GT_OreDictUnificator.get(false, si))) {
                                            tempRecipe.mInputs[j] = new ItemStack(
                                                si.getItem(),
                                                tempRecipe.mInputs[j].stackSize,
                                                si.getItemDamage());
                                        }
                                    }
                                }
                            }
                            box.routingMap.add(new BoxRoutings(tempRecipe, RoutingMachine.getStackForm(1)));
                            box.routingStatus = 0;
                        } else {
                            box.routingStatus = 3;
                        }
                        return;
                    }
                }
            }
        }
        box.routingStatus = 2;
    }

    public static void makeRouting(GT_NEI_DefaultHandler recipe, int recipeIndex, EntityPlayer player) {
        List<PositionedStack> machineListWithPos = RecipeCatalysts.getRecipeCatalysts(recipe);
        List<ItemStack> machineList = machineListWithPos.stream()
            .map(v -> v.item)
            .collect(Collectors.toList());
        for (ItemStack machine : machineList) {
            if (getMetaTileEntity(machine) instanceof GT_MetaTileEntity_MultiBlockBase mte
                && !mte.mName.startsWith("TST")
                && !mte.mName.startsWith("name")) {
                NetworkLoader.instance.sendToServer(
                    new MessageRouting(
                        new BoxRoutings(
                            ((GT_NEI_DefaultHandler.CachedDefaultRecipe) recipe.arecipes.get(recipeIndex)).mRecipe,
                            machine).routingToNbt(),
                        player));
                break;
            }
        }
    }
}
