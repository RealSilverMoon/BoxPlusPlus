package com.silvermoon.boxplusplus.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraftforge.fluids.FluidStack;

public class FluidContainer {

    private final HashMap<String, FluidStack> stack = new HashMap<>();

    public void addFluidStack(FluidStack input, int multiple) {
        String name = input.getUnlocalizedName();
        if (!stack.containsKey(name)) {
            stack.put(name,
                new FluidStack(input.getFluid(), (int) Math.min((long) input.amount * multiple, Integer.MAX_VALUE)));
        } else {
            stack.get(name).amount = (int) Math.min(
                stack.get(name).amount + (long) input.amount * multiple,
                Integer.MAX_VALUE);
        }
    }

    public FluidContainer addFluidStackList(List<FluidStack> list, int multiple) {
        for (FluidStack var1 : list) {
            addFluidStack(var1, multiple);
        }
        return this;
    }

    public List<FluidStack> getFluidStack() {
        List<FluidStack> output = new ArrayList<>();
        stack.forEach((k, v) -> output.add(v));
        return output;
    }
}
