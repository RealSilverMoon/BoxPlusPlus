package com.silvermoon.boxplusplus.common.items;

import static com.silvermoon.boxplusplus.util.Util.i18n;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.silvermoon.boxplusplus.Tags;

import cpw.mods.fml.common.registry.GameRegistry;

public class BoxModuleResearchItem extends Item {

    public BoxModuleResearchItem() {
        setMaxStackSize(1);
        setHasSubtypes(true);
        setMaxDamage(0);
        setTextureName(Tags.MODID + ":" + "box_module_research");
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        int meta = itemstack.getItemDamage();
        return super.getUnlocalizedName() + meta;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
        for (int meta = 0; meta < 9; meta++) {
            list.add(new ItemStack(item, 1, meta));
        }
    }

    public void registerItem() {
        this.setUnlocalizedName("box_module_research");
        GameRegistry.registerItem(this, "box_module_research");
    }

    public static List<String> addWrappedTooltip(String text, int maxLineLength) {
        if (text == null || maxLineLength <= 0) {
            return new ArrayList<>();
        }
        boolean isChinese = false;
        for (char c : text.toCharArray()) {
            if (c >= '\u4e00' && c <= '\u9fa5') {
                isChinese = true;
                break;
            }
        }
        List<String> tooltipLines = new ArrayList<>();
        int length = text.length();
        StringBuilder currentLine = new StringBuilder();
        if (isChinese) {
            for (int i = 0; i < length; i++) {
                currentLine.append(text.charAt(i));

                if (currentLine.length() == maxLineLength) {
                    tooltipLines.add(currentLine.toString());
                    currentLine.setLength(0);
                }
            }
        } else {
            String[] words = text.split(" ");
            for (String word : words) {
                if (currentLine.length() + word.length() + 1 > maxLineLength) {
                    if (currentLine.length() > 0) {
                        tooltipLines.add(
                            currentLine.toString()
                                .trim());
                    }
                    currentLine = new StringBuilder();
                }
                currentLine.append(word)
                    .append(" ");
            }
        }
        if (currentLine.length() > 0) {
            tooltipLines.add(
                currentLine.toString()
                    .trim());
        }
        return tooltipLines;
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean flag) {
        List<String> tooltipLines = addWrappedTooltip(
            i18n("item.box_module_research.tooltip" + itemStack.getItemDamage()),
            15);
        for (String line : tooltipLines) {
            list.add(EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + line);
        }
    }
}
