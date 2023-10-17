package com.silvermoon.boxplusplus.common.items;

import com.silvermoon.boxplusplus.Tags;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import java.util.List;

import static com.silvermoon.boxplusplus.boxplusplus.BoxTab;

public class ItemMain extends Item {

    public ItemMain(String unlocalizedname, String name) {
        this.Name = name;
        this.Unlocalizedname = unlocalizedname;
        super.setMaxStackSize(64);
        super.setUnlocalizedName(Unlocalizedname);
    }

    public IIcon icons;
    private final String Name;
    private final String Unlocalizedname;

    public void registerItem() {
        super.setCreativeTab(BoxTab);
        GameRegistry.registerItem(this, Unlocalizedname);
    }

    @Override
    public void registerIcons(IIconRegister reg) {
        icons = reg.registerIcon(Tags.MODID + ":" + Name);
    }

    @Override
    public IIcon getIconFromDamage(int a) {
        return icons;
    }

    @Override
    public String getUnlocalizedName(ItemStack item) {
        return super.getUnlocalizedName();
    }

    @Override
    public void addInformation(ItemStack item, EntityPlayer player, List list, boolean b) {
        list.add(StatCollector.translateToLocal("tile.boxplusplus_craftitems.desc"));
    }
}
