package com.silvermoon.boxplusplus.util;

import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.structure.IStructureElementNoPlacement;
import com.silvermoon.boxplusplus.common.loader.BlockRegister;
import gregtech.api.util.GT_Recipe;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.function.BiPredicate;
import java.util.function.Function;

public class Util {
    public static String i18n(String info){
        return StatCollector.translateToLocal(info);
    }
    public static GT_Recipe.GT_Recipe_Map getMMRecipeMap(int Mode) {
        return switch (Mode) {
            case 1 -> GT_Recipe.GT_Recipe_Map.sCompressorRecipes;
            case 2 -> GT_Recipe.GT_Recipe_Map.sLatheRecipes;
            case 3 -> GT_Recipe.GT_Recipe_Map.sPolarizerRecipes;
            case 4 -> GT_Recipe.GT_Recipe_Map.sFermentingRecipes;
            case 5 -> GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes;
            case 6 -> GT_Recipe.GT_Recipe_Map.sExtractorRecipes;
            case 7 -> GT_Recipe.GT_Recipe_Map.sLaserEngraverRecipes;
            case 8 -> GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes;
            case 9 -> GT_Recipe.GT_Recipe_Map.sFluidSolidficationRecipes;
            default -> null;
        };
    }
    public static ItemStack findfirstCircuit(ItemStack[] input){
        for(ItemStack item:input){
            if(item.getUnlocalizedName().equals("gt.integrated_circuit")) return item;
        }
        return null;
    }
    //Ah...ha? Forge only use byte to store itemstacksize, which is far enough for box. Let's fix it.
    public static NBTTagCompound writeBoxItemToNBT(ItemStack item,NBTTagCompound nbt)
    {
        nbt.setShort("id", (short) Item.getIdFromItem(item.getItem()));
        nbt.setInteger("Count", item.stackSize);
        nbt.setShort("Damage", (short)item.getItemDamage());

        if (item.stackTagCompound != null)
        {
            nbt.setTag("tag", item.stackTagCompound);
        }

        return nbt;
    }
    public static ItemStack loadBoxItemFromNBT(NBTTagCompound nbt)
    {
        Item boxitem=Item.getItemById(nbt.getShort("id"));
        int stackSize = nbt.getInteger("Count");
        int itemDamage = nbt.getShort("Damage");
        if (itemDamage < 0)
        {
            itemDamage = 0;
        }
        ItemStack boxItem=new ItemStack(boxitem,stackSize,itemDamage);
        if (nbt.hasKey("tag", 10))
        {
            boxItem.stackTagCompound = nbt.getCompoundTag("tag");
        }
        boxItem.func_150996_a(boxitem);
        return boxItem.getItem() != null ? boxItem : null;
    }
    //Auto place TEBlock
    public static <T, E> IStructureElementNoPlacement<T> RingTileAdder(BiPredicate<T, E> iTileAdder,
                                                                       Class<E> tileClass, Block hintBlock, int hintMeta, Function<T,Block> RingAdder) {
        if (iTileAdder == null || hintBlock == null || tileClass == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElementNoPlacement<>() {

            @Override
            public boolean check(T t, World world, int x, int y, int z) {
                TileEntity tileEntity = world.getTileEntity(x, y, z);
                return tileClass.isInstance(tileEntity) && iTileAdder.test(t, tileClass.cast(tileEntity));
            }

            @Override
            public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
                StructureLibAPI.hintParticle(world, x, y, z, hintBlock, hintMeta);
                return true;
            }

            @Override
            public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
                world.setBlock(x, y, z, trigger.stackSize==1?RingAdder.apply(t):(trigger.stackSize==2? BlockRegister.BoxRing2:BlockRegister.BoxRing3), hintMeta, 2);
                return true;
            }
        };
    }
}
