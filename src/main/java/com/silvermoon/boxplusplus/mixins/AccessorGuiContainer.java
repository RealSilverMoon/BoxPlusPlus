package com.silvermoon.boxplusplus.mixins;

import net.minecraft.client.gui.inventory.GuiContainer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = GuiContainer.class)
public interface AccessorGuiContainer {

    @Accessor("guiTop")
    int getGuiTop();
}
