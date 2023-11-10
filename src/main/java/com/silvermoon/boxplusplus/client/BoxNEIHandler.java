package com.silvermoon.boxplusplus.client;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.GuiScreenEvent;

import com.gtnewhorizons.modularui.common.internal.wrapper.ModularGui;
import com.silvermoon.boxplusplus.common.tileentities.GTMachineBox;
import com.silvermoon.boxplusplus.util.BoxRoutings;
import com.silvermoon.boxplusplus.util.Util;

import appeng.client.gui.implementations.GuiPatternTermEx;
import codechicken.nei.GuiNEIButton;
import codechicken.nei.recipe.*;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.nei.GT_NEI_DefaultHandler;

public class BoxNEIHandler {

    private GuiButton[] buttons;
    public static BoxNEIHandler instance = new BoxNEIHandler();
    int recipesPerPage = 2;

    @SubscribeEvent
    public void onDrawScreen(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (event.gui instanceof GuiRecipe gui && gui.firstGui instanceof ModularGui mui
            && mui.getContext()
                .isWindowOpen(10)) {
            EntityPlayer player = ((ModularGui) gui.firstGui).getContext()
                .getPlayer();
            GTMachineBox box = Util.boxMap.get(player);
            if (box == null || box.recipe.islocked) return;
            List buttonList = ObfuscationReflectionHelper.getPrivateValue(GuiScreen.class, gui, "buttonList");
            if (buttons != null) {
                buttonList.removeIf(Arrays.asList(buttons)::contains);
            }
            Method getRecipesPerPage;
            try {
                getRecipesPerPage = GuiRecipe.class.getDeclaredMethod("getRecipesPerPage");
                getRecipesPerPage.setAccessible(true);
                recipesPerPage = (int) getRecipesPerPage.invoke(gui);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {}
            buttons = new GuiButton[recipesPerPage];
            int OVERLAY_BUTTON_ID_START = ObfuscationReflectionHelper
                .getPrivateValue(GuiRecipe.class, gui, "OVERLAY_BUTTON_ID_START");
            int guiTop = ObfuscationReflectionHelper.getPrivateValue(GuiContainer.class, gui, "guiTop");
            int buttonWidth = ObfuscationReflectionHelper.getPrivateValue(GuiRecipe.class, gui, "buttonWidth");
            int buttonHeight = ObfuscationReflectionHelper.getPrivateValue(GuiRecipe.class, gui, "buttonHeight");
            HandlerInfo handlerInfo = ObfuscationReflectionHelper.getPrivateValue(GuiRecipe.class, gui, "handlerInfo");
            for (int i = 0; i < recipesPerPage; i++) {
                buttons[i] = new GuiNEIButton(
                    OVERLAY_BUTTON_ID_START + i,
                    (gui.width / 2) + 65,
                    guiTop + 16 + (handlerInfo.getHeight() * (i + 1)) - 2,
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
                    buttons[i].visible = RecipeInfo.hasOverlayHandler(
                        GuiPatternTermEx.class,
                        gui.getHandler()
                            .getOverlayIdentifier())
                        && gui.getHandler() instanceof GT_NEI_DefaultHandler;
                }
            }
            Collections.addAll(buttonList, buttons);
        }
    }

    @SubscribeEvent
    public void onActionPerformedEventPre(GuiScreenEvent.ActionPerformedEvent.Pre event) {
        if (event.gui instanceof GuiRecipe gui && gui.firstGui instanceof ModularGui mui
            && mui.getContext()
                .isWindowOpen(10)) {
            EntityPlayer player = ((ModularGui) gui.firstGui).getContext()
                .getPlayer();
            List<GuiButton> overlayButtons = new ArrayList<>(
                Arrays.asList(ObfuscationReflectionHelper.getPrivateValue(GuiRecipe.class, gui, "overlayButtons")));
            int OVERLAY_BUTTON_ID_START = ObfuscationReflectionHelper
                .getPrivateValue(GuiRecipe.class, gui, "OVERLAY_BUTTON_ID_START");
            if (event.button.id >= OVERLAY_BUTTON_ID_START
                && event.button.id < OVERLAY_BUTTON_ID_START + overlayButtons.size()) {
                IRecipeHandler handler = (IRecipeHandler) gui.currenthandlers.get(gui.recipetype);
                if (recipesPerPage >= 0 && handler != null) {
                    int recipe = gui.page * recipesPerPage + event.button.id - OVERLAY_BUTTON_ID_START;
                    BoxRoutings
                        .makeRouting((GT_NEI_DefaultHandler) gui.currenthandlers.get(gui.recipetype), recipe, player);
                    event.setCanceled(true);
                }
            }
        }
    }
}
