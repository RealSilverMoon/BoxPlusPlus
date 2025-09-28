package com.silvermoon.boxplusplus.client;

import java.util.*;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.GuiScreenEvent;

import com.gtnewhorizons.modularui.common.internal.wrapper.ModularGui;
import com.silvermoon.boxplusplus.common.tileentities.GTMachineBox;
import com.silvermoon.boxplusplus.mixins.AccessorGuiContainer;
import com.silvermoon.boxplusplus.mixins.AccessorGuiRecipe;
import com.silvermoon.boxplusplus.mixins.AccessorGuiScreen;
import com.silvermoon.boxplusplus.util.BoxRoutings;
import com.silvermoon.boxplusplus.util.Util;

import codechicken.nei.GuiNEIButton;
import codechicken.nei.recipe.*;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.nei.GTNEIDefaultHandler;

public class BoxNEIHandler {

    private GuiButton[] buttons;
    public static BoxNEIHandler instance = new BoxNEIHandler();
    int recipesPerPage = 2;

    @SubscribeEvent
    public void onDrawScreen(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (event.gui instanceof GuiRecipe<?>gui && gui.firstGui instanceof ModularGui mui
            && mui.getContext()
                .isWindowOpen(10)) {
            EntityPlayer player = mui.getContext()
                .getPlayer();
            GTMachineBox box = Util.boxMap.get(player);
            if (box == null || box.getBaseMetaTileEntity()
                .isDead() || box.recipe.islocked) return;
            List<GuiButton> buttonList = ((AccessorGuiScreen) gui).getButtonList();
            if (buttons != null) {
                buttonList.removeIf(Arrays.asList(buttons)::contains);
            }
            recipesPerPage = ((AccessorGuiRecipe) gui).invokeGetRecipesPerPage();
            buttons = new GuiButton[recipesPerPage];
            int OVERLAY_BUTTON_ID_START = 4;
            int guiTop = ((AccessorGuiContainer) gui).getGuiTop();
            int buttonWidth = ((AccessorGuiRecipe) gui).getButtonWidth();
            int buttonHeight = ((AccessorGuiRecipe) gui).getButtonHeight();
            HandlerInfo handlerInfo = ((AccessorGuiRecipe) gui).getHandlerInfo();
            for (int i = 0; i < recipesPerPage; i++) {
                buttons[i] = new GuiNEIButton(
                    OVERLAY_BUTTON_ID_START + i,
                    (gui.width / 2) + 69,
                    guiTop + ((handlerInfo.getHeight() - handlerInfo.getYShift()) * (i + 1)),
                    buttonWidth,
                    buttonHeight,
                    "B");
            }
            int counts = Math.min(
                gui.getHandler()
                    .numRecipes() - (gui.page * recipesPerPage),
                recipesPerPage);
            for (int i = 0; i < buttons.length; i++) {
                if (i >= counts) {
                    buttons[i].visible = false;
                } else {
                    buttons[i].visible = gui.getHandler() instanceof GTNEIDefaultHandler;
                }
            }
            Collections.addAll(buttonList, buttons);
        }
    }

    @SubscribeEvent
    public void onActionPerformedEventPre(GuiScreenEvent.ActionPerformedEvent.Pre event) {
        if (event.gui instanceof GuiRecipe<?>gui && gui.firstGui instanceof ModularGui mui
            && mui.getContext()
                .isWindowOpen(10)) {
            EntityPlayer player = mui.getContext()
                .getPlayer();
            List<GuiButton> overlayButtons = new ArrayList<>(
                Arrays.asList(((AccessorGuiRecipe) gui).getOverlayButtons()));
            int OVERLAY_BUTTON_ID_START = 4;
            if (event.button.id >= OVERLAY_BUTTON_ID_START
                && event.button.id < OVERLAY_BUTTON_ID_START + overlayButtons.size()) {
                IRecipeHandler handler = gui.currenthandlers.get(gui.recipetype);
                if (recipesPerPage >= 0 && handler != null) {
                    int recipe = gui.page * recipesPerPage + event.button.id - OVERLAY_BUTTON_ID_START;
                    BoxRoutings
                        .makeRouting((GTNEIDefaultHandler) gui.currenthandlers.get(gui.recipetype), recipe, player);
                    event.setCanceled(true);
                }
            }
        }
    }
}
