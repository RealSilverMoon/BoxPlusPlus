package com.silvermoon.boxplusplus.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import codechicken.nei.recipe.GuiOverlayButton;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.HandlerInfo;

@Mixin(value = GuiRecipe.class, remap = false)
public interface AccessorGuiRecipe {

    @Invoker("getRecipesPerPage")
    int invokeGetRecipesPerPage();

    @Accessor("BUTTON_HEIGHT")
    int getButtonHeight();

    @Accessor("BUTTON_WIDTH")
    int getButtonWidth();

    @Accessor("handlerInfo")
    HandlerInfo getHandlerInfo();

    @Accessor("overlayButtons")
    GuiOverlayButton[] getOverlayButtons();
}
