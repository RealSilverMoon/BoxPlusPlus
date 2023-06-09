package com.silvermoon.boxplusplus.util;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemContainer {
    private final Table<Item,Integer,Integer> stack = HashBasedTable.create();

    public void addItemStack(ItemStack input,int chance) {
        if (stack.containsRow(input.getItem())&&stack.containsColumn(input.getItemDamage())) {
            stack.put(input.getItem(),input.getItemDamage(),chance*input.stackSize+stack.get(input.getItem(),input.getItemDamage()));
        } else {
            stack.put(input.getItem(), input.getItemDamage(),chance*input.stackSize);
        }
    }
    public ItemContainer addItemStackList(List<ItemStack> list) {
        for (ItemStack var1 : list) {
            if(var1==null)continue;
            addItemStack(var1,10000);
        }
        return this;
    }
    public void addItemStackList(List<ItemStack> list, List<Integer> chance) {
        for (int i=0;i<list.size();i++) {
            addItemStack(list.get(i),chance.get(i));
        }
    }

    public List<ItemStack> getItemStack() {
        List<ItemStack> output = new ArrayList<>();
        for(Item item:stack.rowKeySet()){
            for(int meta:stack.columnKeySet()){
                if(stack.get(item,meta)!=null){
                    output.add(new ItemStack(item,
                        (int)Math.floor(stack.get(item,meta))/10000,meta));
                }
            }
        }
        return output;
    }
}
