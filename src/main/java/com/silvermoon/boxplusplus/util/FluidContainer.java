package com.silvermoon.boxplusplus.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class FluidContainer {

    private final Map<FluidKey, FluidStack> stacks = new HashMap<>();

    public void addFluidStack(FluidStack input, int multiple) {
        if (input == null || input.getFluid() == null) return;

        NBTTagCompound nbt = input.tag != null ? (NBTTagCompound) input.tag.copy() : null;

        FluidKey key = new FluidKey(input.getFluid(), nbt);
        int amountToAdd = input.amount * multiple;

        if (stacks.containsKey(key)) {
            FluidStack stack = stacks.get(key);
            stack.amount = (int) Math.min((long) stack.amount + amountToAdd, Integer.MAX_VALUE);
        } else {
            FluidStack copied = new FluidStack(input, amountToAdd);
            copied.tag = nbt;
            stacks.put(key, copied);
        }
    }

    public FluidContainer addFluidStackList(List<FluidStack> list, int multiple) {
        for (FluidStack fluidStack : list) {
            if (fluidStack != null) addFluidStack(fluidStack, multiple);
        }
        return this;
    }

    public List<FluidStack> getFluidStack() {
        return new ArrayList<>(stacks.values());
    }

    private static class FluidKey {

        private final String fluidName;
        private final NBTTagCompound nbt;
        private final int hashCode;

        public FluidKey(Fluid fluid, NBTTagCompound nbt) {
            this.fluidName = fluid.getName();
            this.nbt = nbt != null ? (NBTTagCompound) nbt.copy() : null;

            this.hashCode = 31 * fluidName.hashCode() + (nbt != null ? nbt.hashCode() : 0);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            FluidKey other = (FluidKey) obj;
            return fluidName.equals(other.fluidName) && nbtEquals(nbt, other.nbt);
        }

        @Override
        public int hashCode() {
            return hashCode;
        }

        private boolean nbtEquals(NBTTagCompound nbt1, NBTTagCompound nbt2) {
            if (nbt1 == nbt2) return true;
            if (nbt1 == null || nbt2 == null) return false;
            return nbt1.equals(nbt2);
        }
    }
}
