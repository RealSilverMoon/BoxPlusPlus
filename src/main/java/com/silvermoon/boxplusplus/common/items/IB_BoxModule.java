package com.silvermoon.boxplusplus.common.items;

import com.silvermoon.boxplusplus.common.block.BlockBoxModuleCore;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

import static com.silvermoon.boxplusplus.util.Util.i18n;

public class IB_BoxModule extends ItemBlock {

    public IB_BoxModule(Block block) {
        super(block);
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }

    @Override
    public boolean getHasSubtypes() {
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_) {
        boolean update = ((BlockBoxModuleCore) field_150939_a).isUpdate;
        list.add(
            EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD
                + i18n("tile.boxplusplus.boxUI.module.context." + (stack.getItemDamage() + 1) + (update ? "f" : "a")));
        if (update) {
            list.add(
                EnumChatFormatting.GOLD + i18n("tile.boxplusplus.boxUI.module." + (stack.getItemDamage() + 1))
                    + " (T2)");
        }
        list.add(i18n("tile.boxplusplus.boxUI.module.context." + (stack.getItemDamage() + 1) + (update ? "d" : "b")));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "." + stack.getItemDamage();
    }
}
