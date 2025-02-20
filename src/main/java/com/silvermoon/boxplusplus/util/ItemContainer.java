package com.silvermoon.boxplusplus.util;

import java.util.*;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemContainer {

    private final Map<ItemStackKey, Long> stack = new HashMap<>();

    public void addItemStack(ItemStack input, int multiple, int chance) {
        if (input == null || input.getItem() == null) return;

        NBTTagCompound nbt = input.getTagCompound() != null ? (NBTTagCompound) input.getTagCompound()
            .copy() : null;

        ItemStackKey key = new ItemStackKey(input.getItem(), input.getItemDamage(), nbt);
        long increment = (long) input.stackSize * multiple * chance;

        stack.put(key, stack.getOrDefault(key, 0L) + increment);
    }

    public ItemContainer addItemStackList(List<ItemStack> list, int multiple) {
        for (ItemStack stack : list) {
            if (stack == null) continue;
            addItemStack(stack, multiple, 10000);
        }
        return this;
    }

    public void addItemStackList(List<ItemStack> list, List<Integer> chances, int multiple) {
        for (int i = 0; i < list.size(); i++) {
            ItemStack stack = list.get(i);
            if (stack != null) {
                int chance = (i < chances.size()) ? chances.get(i) : 10000;
                addItemStack(stack, multiple, chance);
            }
        }
    }

    public List<ItemStack> getItemStack() {
        List<ItemStack> output = new ArrayList<>();
        for (Map.Entry<ItemStackKey, Long> entry : stack.entrySet()) {
            ItemStackKey key = entry.getKey();
            long total = entry.getValue();
            int count = (int) Math.min(total / 10000, Integer.MAX_VALUE - 1);

            ItemStack stack = new ItemStack(key.getItem(), count, key.getMeta());
            if (key.getNbt() != null) {
                stack.setTagCompound(
                    (NBTTagCompound) key.getNbt()
                        .copy());
            }
            output.add(stack);
        }
        return output;
    }

    private static class ItemStackKey {

        private final Item item;
        private final int meta;
        private final NBTTagCompound nbt;

        public ItemStackKey(Item item, int meta, NBTTagCompound nbt) {
            this.item = item;
            this.meta = meta;
            this.nbt = nbt;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            ItemStackKey other = (ItemStackKey) obj;
            return meta == other.meta && item.equals(other.item) && nbtEquals(nbt, other.nbt);
        }

        @Override
        public int hashCode() {
            int hash = item.hashCode() ^ meta;
            return nbt != null ? hash ^ nbt.hashCode() : hash;
        }

        private boolean nbtEquals(NBTTagCompound nbt1, NBTTagCompound nbt2) {
            if (nbt1 == nbt2) return true;
            if (nbt1 == null || nbt2 == null) return false;
            return nbt1.equals(nbt2);
        }

        public Item getItem() {
            return item;
        }

        public int getMeta() {
            return meta;
        }

        public NBTTagCompound getNbt() {
            return nbt;
        }
    }
}
