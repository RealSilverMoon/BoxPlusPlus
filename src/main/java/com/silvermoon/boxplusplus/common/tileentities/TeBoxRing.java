package com.silvermoon.boxplusplus.common.tileentities;

import cpw.mods.fml.relauncher.Side;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAdvanced;
import micdoodle8.mods.galacticraft.core.util.Annotations;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;

public class TeBoxRing extends TileEntityAdvanced {

    @Annotations.NetworkedField(targetSide = Side.CLIENT)
    public double Rotation = 0;
    @Annotations.NetworkedField(targetSide = Side.CLIENT)
    public boolean renderStatus = false;
    @Annotations.NetworkedField(targetSide = Side.CLIENT)
    public boolean teRingSwitch = true;

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return 65536;
    }

    public double getCurrentRotation() {
        return Rotation;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setBoolean("renderStatus", renderStatus);
        nbt.setDouble("Rotation", Rotation);
        nbt.setBoolean("switch", teRingSwitch);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        renderStatus = nbt.getBoolean("renderStatus");
        Rotation = nbt.getDouble("Rotation");
        teRingSwitch = nbt.getBoolean("switch");
    }

    @Override
    public double getPacketRange() {
        return 128;
    }

    @Override
    public int getPacketCooldown() {
        return 20;
    }

    @Override
    public boolean isNetworkedTile() {
        return true;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        Rotation = (Rotation + 1.2) % 360d;
    }
}
