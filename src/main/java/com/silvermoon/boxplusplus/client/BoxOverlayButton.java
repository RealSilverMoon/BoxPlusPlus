package com.silvermoon.boxplusplus.client;

import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;

import com.gtnewhorizons.modularui.common.internal.wrapper.ModularGui;
import com.silvermoon.boxplusplus.common.tileentities.GTMachineBox;
import com.silvermoon.boxplusplus.util.BoxRoutings;
import com.silvermoon.boxplusplus.util.Util;

import codechicken.nei.recipe.GuiOverlayButton;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.GuiRecipeButton;
import codechicken.nei.recipe.RecipeHandlerRef;
import gregtech.nei.GTNEIDefaultHandler;

public class BoxOverlayButton extends GuiOverlayButton {

    public BoxOverlayButton(GuiContainer firstGui, RecipeHandlerRef handlerRef, int xPosition, int yPosition) {
        super(firstGui, handlerRef, xPosition, yPosition);
    }

    public BoxOverlayButton(GuiOverlayButton button) {
        this(button.firstGui, button.handlerRef, button.xPosition, button.yPosition);
    }

    @Override
    public boolean canFillCraftingGrid() {
        return true;
    }

    @Override
    public boolean hasOverlay() {
        return true;
    }

    @Override
    public void mouseReleased(int mousex, int mousey) {
        if (this.firstGui instanceof ModularGui mui && mui.getContext()
            .isWindowOpen(10)) {
            EntityPlayer player = mui.getContext()
                .getPlayer();
            GTMachineBox box = Util.boxMap.get(player);
            if (box == null || box.getBaseMetaTileEntity()
                .isDead() || box.recipe.islocked) {
                super.mouseReleased(mousex, mousey);
                return;
            }
            if (this.handlerRef.handler instanceof GTNEIDefaultHandler) {
                BoxRoutings
                    .makeRouting((GTNEIDefaultHandler) this.handlerRef.handler, this.handlerRef.recipeIndex, player);
                return;
            }
        }
        super.mouseReleased(mousex, mousey);
    }

    public static void updateRecipeButtons(GuiRecipe<?> guiRecipe, List<GuiRecipeButton> buttonList) {
        for (int i = 0; i < buttonList.size(); i++) {
            if (buttonList.get(i) instanceof GuiOverlayButton btn && !(btn instanceof BoxOverlayButton)) {
                buttonList.set(i, new BoxOverlayButton(btn));
            }
        }
    }
}
