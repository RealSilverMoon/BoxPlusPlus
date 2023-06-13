package com.silvermoon.boxplusplus.util;

import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.util.GT_Recipe;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.silvermoon.boxplusplus.util.Util.loadBoxItemFromNBT;
import static com.silvermoon.boxplusplus.util.Util.writeBoxItemToNBT;

public class BoxRoutings {
    public ItemStack RoutingMachine;
    public List<ItemStack> InputItem = new ArrayList<>();
    public List<ItemStack> OutputItem = new ArrayList<>();
    public List<Integer> OutputChance = new ArrayList<>();
    public List<FluidStack> InputFluid = new ArrayList<>();
    public List<FluidStack> OutputFluid = new ArrayList<>();
    public int Parallel = 1;
    public int time;
    public Long voltage;

    public BoxRoutings(GT_Recipe recipe, GT_MetaTileEntity_MultiBlockBase teMachine ) {
        ItemStack machine = teMachine.getStackForm(1);
        InputItem.addAll(Arrays.asList(recipe.mInputs));
        InputItem.removeAll(Collections.singleton(null));
        OutputItem.addAll(Arrays.asList(recipe.mOutputs));
        if(teMachine.mName.equals("multimachine.plasmaforge")){
            for(int i=0;i<OutputItem.size();i++) OutputChance.add(7500);
        }else for(int i=0;i<OutputItem.size();i++) OutputChance.add(recipe.getOutputChance(i));
        OutputItem.removeAll(Collections.singleton(null));
        InputFluid.addAll(Arrays.asList(recipe.mFluidInputs));
        OutputFluid.addAll(Arrays.asList(recipe.mFluidOutputs));
        RoutingMachine = machine;
        voltage = (long) recipe.mEUt;
        time = recipe.mDuration;
        //do some special service
        switch (teMachine.mName) {
            case "multimachine.plasmaforge" -> {
                time *= 4;
                OutputFluid.forEach(f -> f.amount = (int) (f.amount * 0.75));
            }
            case "componentassemblyline" -> time /= 16;
            case "quantumforcetransformer.controller.tier.single" ->
                OutputFluid.forEach(f->f.amount= f.amount*(1/(OutputFluid.size()+OutputItem.size())));
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
    public BoxRoutings(InventoryCrafting inputs, ItemStack outputs, ItemStack machine) {
        for(int i=0;i<9;i++){
            if(inputs.getStackInSlot(i)==null)break;
            InputItem.add(inputs.getStackInSlot(i).copy());
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
            OutputChance.add(routingNBT.getInteger("OutputChance"+(i+1)));
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
    }

    public NBTTagCompound routingToNbt() {
        NBTTagCompound routing = new NBTTagCompound();
        routing.setTag("machine", RoutingMachine.writeToNBT(new NBTTagCompound()));
        for (int i = 0; i < InputItem.size(); i++)
            routing.setTag("InputItem" + (i + 1), writeBoxItemToNBT(InputItem.get(i),new NBTTagCompound()));
        for (int i = 0; i < OutputItem.size(); i++) {
            routing.setTag("OutputItem" + (i + 1), writeBoxItemToNBT(OutputItem.get(i),new NBTTagCompound()));
            routing.setInteger("OutputChance" + (i + 1), OutputChance.get(i));
        }
        for (int i = 0; i < InputFluid.size(); i++)
            routing.setTag("InputFluid" + (i + 1), InputFluid.get(i).writeToNBT(new NBTTagCompound()));
        for (int i = 0; i < OutputFluid.size(); i++)
            routing.setTag("OutputFluid" + (i + 1), OutputFluid.get(i).writeToNBT(new NBTTagCompound()));
        routing.setLong("Voltage", voltage);
        routing.setInteger("Parallel", Parallel);
        routing.setInteger("Time",time);
        return routing;
    }

    public int calHeight() {
        return InputItem.size() + OutputItem.size() + InputFluid.size() + OutputFluid.size();
    }
}
