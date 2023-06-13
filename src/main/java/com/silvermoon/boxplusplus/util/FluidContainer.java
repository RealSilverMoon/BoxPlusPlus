package com.silvermoon.boxplusplus.util;

import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FluidContainer {
    private final HashMap<String , FluidStack> stack = new HashMap<>();

    public void addFluidStack(FluidStack input) {
        String name=input.getUnlocalizedName();
        if (!stack.containsKey(name)) {
            stack.put(name, input.copy());
        } else {
            stack.get(name).amount=(int)Math.min((long)stack.get(name).amount+input.amount,Integer.MAX_VALUE);
        }
    }

    public FluidContainer addFluidStackList(List<FluidStack> list) {
        for (FluidStack var1 : list) {
            addFluidStack(var1);
        }
        return this;
    }

    public List<FluidStack> getFluidStack() {
        List<FluidStack> output = new ArrayList<>();
        stack.forEach((k,v)-> output.add(v));
        return output;
    }
}
