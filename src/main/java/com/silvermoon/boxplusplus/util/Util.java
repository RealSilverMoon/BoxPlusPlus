package com.silvermoon.boxplusplus.util;

import java.io.IOException;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.HashMultimap;
import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.silvermoon.boxplusplus.common.loader.BlockRegister;
import com.silvermoon.boxplusplus.common.tileentities.GTMachineBox;
import com.silvermoon.boxplusplus.common.tileentities.GTMachineDroneMaintainingCentre;

import appeng.api.AEApi;
import appeng.api.definitions.IDefinitions;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_ModHandler;

public class Util {

    public static HashMap<EntityPlayer, GTMachineBox> boxMap = new HashMap<>();
    public static HashMultimap<Integer, GTMachineDroneMaintainingCentre> droneMap = HashMultimap.create();

    public static String i18n(String info) {
        return StatCollector.translateToLocal(info)
            .replace("&", "§");
    }

    public static RecipeMap<RecipeMapBackend> getMMRecipeMap(int Mode) {
        return switch (Mode) {
            case 1 -> RecipeMaps.compressorRecipes;
            case 2 -> RecipeMaps.latheRecipes;
            case 3 -> RecipeMaps.polarizerRecipes;
            case 4 -> RecipeMaps.fermentingRecipes;
            case 5 -> RecipeMaps.fluidExtractionRecipes;
            case 6 -> RecipeMaps.extractorRecipes;
            case 7 -> RecipeMaps.laserEngraverRecipes;
            case 8 -> RecipeMaps.autoclaveRecipes;
            case 9 -> RecipeMaps.fluidSolidifierRecipes;
            default -> null;
        };
    }

    public static ItemStack findfirstCircuit(List<ItemStack> input) {
        for (ItemStack item : input) {
            if (item.getUnlocalizedName()
                .equals("gt.integrated_circuit")) return item;
        }
        return null;
    }

    public static boolean isPattern(final ItemStack output) {
        if (output == null) {
            return false;
        }

        final IDefinitions definitions = AEApi.instance()
            .definitions();

        boolean isPattern = definitions.items()
            .encodedPattern()
            .isSameAs(output);
        isPattern |= definitions.materials()
            .blankPattern()
            .isSameAs(output);

        return isPattern;
    }

    public static NBTBase createItemTag(final ItemStack i) {
        final NBTTagCompound c = new NBTTagCompound();

        if (i != null) {
            i.writeToNBT(c);
            c.setInteger("Count", i.stackSize);
        }

        return c;
    }

    // Ah...ha? Forge only use byte to store itemstacksize, which is far enough for box. Let's fix it.
    public static NBTTagCompound writeBoxItemToNBT(ItemStack item, NBTTagCompound nbt) {
        nbt.setShort("id", (short) Item.getIdFromItem(item.getItem()));
        nbt.setInteger("Count", item.stackSize);
        nbt.setShort("Damage", (short) item.getItemDamage());

        if (item.stackTagCompound != null) {
            nbt.setTag("tag", item.stackTagCompound);
        }

        return nbt;
    }

    public static NBTTagCompound writeBoxItemToUNBT(ItemStack item, NBTTagCompound nbt) {
        String registerName = item.getItem().delegate.name();
        nbt.setString("modID", registerName.substring(0, registerName.indexOf(':')));
        nbt.setString("name", registerName.substring(registerName.indexOf(':') + 1));
        nbt.setInteger("Count", item.stackSize);
        nbt.setShort("Damage", (short) item.getItemDamage());

        if (item.stackTagCompound != null) {
            nbt.setTag("tag", item.stackTagCompound);
        }

        return nbt;
    }

    public static ItemStack readBoxItemFromUNBT(NBTTagCompound nbt) {
        int stackSize = nbt.getInteger("Count");
        int itemDamage = nbt.getShort("Damage");
        if (itemDamage < 0) {
            itemDamage = 0;
        }
        ItemStack boxItem = GT_ModHandler
            .getModItem(nbt.getString("modID"), nbt.getString("name"), stackSize, itemDamage);
        if (nbt.hasKey("tag", 10)) {
            boxItem.stackTagCompound = nbt.getCompoundTag("tag");
        }
        return boxItem;
    }

    public static ItemStack loadBoxItemFromNBT(NBTTagCompound nbt) {
        Item boxitem = Item.getItemById(nbt.getShort("id"));
        int stackSize = nbt.getInteger("Count");
        int itemDamage = nbt.getShort("Damage");
        if (itemDamage < 0) {
            itemDamage = 0;
        }
        ItemStack boxItem = new ItemStack(boxitem, stackSize, itemDamage);
        if (nbt.hasKey("tag", 10)) {
            boxItem.stackTagCompound = nbt.getCompoundTag("tag");
        }
        boxItem.func_150996_a(boxitem);
        return boxItem.getItem() != null ? boxItem : null;
    }

    // Auto place TEBlock
    public static <T, E> IStructureElement<T> RingTileAdder(BiPredicate<T, E> iTileAdder, Class<E> tileClass,
        Block hintBlock, int hintMeta, Function<T, Block> RingAdder) {
        if (iTileAdder == null || hintBlock == null || tileClass == null) {
            throw new IllegalArgumentException();
        }
        return new IStructureElement<>() {

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
                world.setBlock(
                    x,
                    y,
                    z,
                    trigger.stackSize == 1 ? RingAdder.apply(t)
                        : (trigger.stackSize == 2 ? BlockRegister.BoxRing2 : BlockRegister.BoxRing3),
                    hintMeta,
                    2);
                return true;
            }
        };
    }

    public static String serialize(NBTTagCompound nbt) {
        try {
            return org.apache.commons.codec.binary.Base64.encodeBase64String(CompressedStreamTools.compress(nbt));
        } catch (IOException ignored) {}
        return null;
    }

    public static @Nullable NBTTagCompound deserialize(String str) {
        if (StringUtils.isNotEmpty(str)) {
            byte[] b = org.apache.commons.codec.binary.Base64.decodeBase64(str);
            try {
                return CompressedStreamTools.func_152457_a(b, new NBTSizeTracker(2097152L));
            } catch (IOException ignored) {}
        }
        return null;
    }

    public static List<ItemStack> deepCopyItemList(List<ItemStack> org) {
        List<ItemStack> newList = new ArrayList<>();
        if (org == null) return newList;
        for (ItemStack t : org) {
            newList.add(t.copy());
        }
        return newList;
    }

    public static List<FluidStack> deepCopyFluidList(List<FluidStack> org) {
        List<FluidStack> newList = new ArrayList<>();
        if (org == null) return newList;
        for (FluidStack t : org) {
            newList.add(t.copy());
        }
        return newList;
    }

    public static String validator(BoxRecipe recipe, String var, boolean isFluid) {
        // 0 means 0
        if (var.equals("0")) return var;
        // No Duplicate Numbers
        if (hasDuplicateNumbers(var)) return "";
        // wrong format?
        Matcher m = Pattern.compile("^[0-9-,]+$")
            .matcher(var);
        if (!m.matches()) return "";
        // The second number cannot bigger then the first one!
        Matcher matcher = Pattern.compile("\\d+-\\d+")
            .matcher(var);
        if (matcher.find()) {
            boolean isSmaller = Stream.iterate(true, b -> b)
                .limit(10)
                .map(match -> match ? matcher.group() : null)
                .filter(Objects::nonNull)
                .map(match -> {
                    String[] parts = match.split("-");
                    int num1 = Integer.parseInt(parts[0]);
                    int num2 = Integer.parseInt(parts[1]);
                    return num1 < num2;
                })
                .reduce((x, y) -> x && y)
                .orElse(false);
            if (!isSmaller) return "";
        }
        // out of boundary
        boolean isOutArray = Stream.of(var.split(","))
            .flatMap(s -> Arrays.stream(s.split("-")))
            .map(Integer::parseInt)
            .max(Integer::compareTo)
            .map(max -> max > (isFluid ? recipe.FinalFluidOutput : recipe.FinalItemOutput).size())
            .orElse(true);
        return isOutArray ? "" : var;
    }

    public static boolean hasDuplicateNumbers(String input) {
        Set<Character> seen = new HashSet<>();
        for (char c : input.toCharArray()) {
            if (Character.isDigit(c)) {
                if (seen.contains(c)) {
                    return true;
                }
                seen.add(c);
            }
        }
        return false;
    }

    public static EntityPlayer getPlayerFromUUID(String uuid) {
        for (Object player : MinecraftServer.getServer()
            .getConfigurationManager().playerEntityList) {
            if (player instanceof EntityPlayer player1) {
                if (uuid.equals(
                    player1.getUniqueID()
                        .toString()))
                    return player1;
            }
        }
        return null;
    }
}
