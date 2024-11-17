package com.silvermoon.boxplusplus.util;

import static com.silvermoon.boxplusplus.util.Util.i18n;

import javax.annotation.Nonnull;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumChatFormatting;

import org.jetbrains.annotations.NotNull;

import gregtech.api.recipe.check.CheckRecipeResult;

public class ResultModuleRequirement implements CheckRecipeResult {

    private int required;
    private boolean isUpdated;

    public ResultModuleRequirement(int required, boolean isUpdated) {
        this.required = required;
        this.isUpdated = isUpdated;
    }

    @Override
    public String getID() {
        return "module_requirement";
    }

    @Override
    public boolean wasSuccessful() {
        return false;
    }

    @Override
    public String getDisplayString() {
        return EnumChatFormatting.AQUA + i18n("tile.boxplusplus.boxUI.37") + i18n("tile.boxplusplus_" + (isUpdated
            ? "boxmoduleplus."
            : "boxmodule." + required + ".name"));
    }

    @NotNull
    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound tag) {
        return null;
    }

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound tag) {

    }

    @Override
    public CheckRecipeResult newInstance() {
        return new ResultModuleRequirement(0, false);
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeVarIntToBuffer(required);
        buffer.writeBoolean(isUpdated);
    }

    @Override
    public void decode(PacketBuffer buffer) {
        required = buffer.readVarIntFromBuffer();
        isUpdated = buffer.readBoolean();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultModuleRequirement that = (ResultModuleRequirement) o;
        return required == that.required && (isUpdated == that.isUpdated);
    }
}
