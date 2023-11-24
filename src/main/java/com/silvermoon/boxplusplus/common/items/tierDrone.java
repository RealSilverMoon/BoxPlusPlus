package com.silvermoon.boxplusplus.common.items;

import static com.silvermoon.boxplusplus.boxplusplus.BoxTab;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.silvermoon.boxplusplus.Tags;
import com.silvermoon.boxplusplus.util.Util;

import cpw.mods.fml.common.registry.GameRegistry;

public class tierDrone extends Item {

    public tierDrone(String name) {
        this.name = name;
        setMaxStackSize(64);
        setUnlocalizedName(name);
    }

    public final IIcon[] icons = new IIcon[3];
    private final String name;

    public void registerItem() {
        super.setCreativeTab(BoxTab);
        GameRegistry.registerItem(this, name);
    }

    @Override
    public void registerIcons(IIconRegister reg) {
        for (int i = 0; i < 3; i++) {
            icons[i] = reg.registerIcon(Tags.MODID + ":maintainingDrone" + i);
        }
    }

    @Override
    public IIcon getIconFromDamage(int a) {
        return icons[a];
    }

    @Override
    public String getUnlocalizedName(ItemStack item) {
        return super.getUnlocalizedName() + item.getItemDamage();
    }

    @Override
    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List p_150895_3_) {
        for (int i = 0; i < 3; i++) {
            p_150895_3_.add(new ItemStack(p_150895_1_, 1, i));
        }
    }

    @Override
    public int getMetadata(int p_77647_1_) {
        return p_77647_1_;
    }

    @Override
    public void addInformation(ItemStack item, EntityPlayer player, List list, boolean b) {
        list.add(
            Util.i18n(
                ("tile.boxplusplus_maintainingDrone%.desc".replaceAll("%", String.valueOf(item.getItemDamage())))));
    }
}
