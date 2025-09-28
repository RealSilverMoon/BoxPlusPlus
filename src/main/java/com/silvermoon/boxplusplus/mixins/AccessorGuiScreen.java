package com.silvermoon.boxplusplus.mixins;

import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = GuiScreen.class)
public interface AccessorGuiScreen {

    @Accessor("buttonList")
    List<GuiButton> getButtonList();
}
