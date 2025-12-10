package com.silvermoon.boxplusplus.client;

import net.minecraft.entity.player.EntityPlayer;

import com.gtnewhorizons.modularui.common.internal.wrapper.ModularGui;
import com.silvermoon.boxplusplus.common.tileentities.GTMachineBox;
import com.silvermoon.boxplusplus.util.Util;

import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.GuiRecipeButton;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.nei.GTNEIDefaultHandler;

public class BoxNEIHandler {

    public static final BoxNEIHandler instance = new BoxNEIHandler();

    private BoxNEIHandler() {}

    @SubscribeEvent
    public void onUpdateRecipeButtons(GuiRecipeButton.UpdateRecipeButtonsEvent.Post event) {
        if (event.gui instanceof GuiRecipe<?>gui) {
            if (isGuiEligible(gui)) {
                BoxOverlayButton.updateRecipeButtons(gui, event.buttonList);
            }
        }
    }

    private boolean isGuiEligible(GuiRecipe<?> gui) {
        if (gui.firstGui instanceof ModularGui mui && mui.getContext()
            .isWindowOpen(10)) {
            EntityPlayer player = mui.getContext()
                .getPlayer();
            GTMachineBox box = Util.boxMap.get(player);
            if (box == null || box.getBaseMetaTileEntity()
                .isDead() || box.recipe.islocked) {
                return false;
            }
            return gui.getHandler() instanceof GTNEIDefaultHandler;
        }
        return false;
    }
}
