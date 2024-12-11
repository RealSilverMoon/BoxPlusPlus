package com.silvermoon.boxplusplus.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class ItemContainer {

    private final Table<Item, Integer, Long> stack = HashBasedTable.create();

    public void addItemStack(ItemStack input, int multiple, int chance) {
        if (input.getItem() == null) return;
        Optional<Long> isNull = Optional.ofNullable(stack.get(input.getItem(), input.getItemDamage()));
        if (isNull.isPresent()) {
            stack
                .put(input.getItem(), input.getItemDamage(), chance * (long) input.stackSize * multiple + isNull.get());
        } else {
            stack.put(input.getItem(), input.getItemDamage(), chance * (long) input.stackSize * multiple);
        }
    }

    public ItemContainer addItemStackList(List<ItemStack> list, int multiple) {
        for (ItemStack var1 : list) {
            if (var1 == null) continue;
            addItemStack(var1, multiple, 10000);
        }
        return this;
    }

    public void addItemStackList(List<ItemStack> list, List<Integer> chance, int multiple) {
        for (int i = 0; i < list.size(); i++) {
            addItemStack(list.get(i), multiple, chance.get(i));
        }
    }

    public List<ItemStack> getItemStack() {
        List<ItemStack> output = new ArrayList<>();
        for (Item item : stack.rowKeySet()) {
            for (int meta : stack.columnKeySet()) {
                if (stack.get(item, meta) != null) {
                    output.add(
                        new ItemStack(
                            item,
                            (int) Math.min(stack.get(item, meta) / 10000, Integer.MAX_VALUE - 1),
                            meta));
                }
            }
        }
        return output;
    }
}
