package com.silvermoon.boxplusplus.common.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;

import cpw.mods.fml.relauncher.Side;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAdvanced;
import micdoodle8.mods.galacticraft.core.util.Annotations;

public class TEBoxRing extends TileEntityAdvanced {

    @Annotations.NetworkedField(targetSide = Side.CLIENT)
    public double scale = 1;

    @Annotations.NetworkedField(targetSide = Side.CLIENT)
    public double rotation = 0;

    @Annotations.NetworkedField(targetSide = Side.CLIENT)
    public double prevRotation = 0;

    @Annotations.NetworkedField(targetSide = Side.CLIENT)
    public boolean renderStatus = false;

    @Annotations.NetworkedField(targetSide = Side.CLIENT)
    public boolean teRingSwitch = true;

    private static final double ROTATION_SPEED = 1.2;

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return 65536;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setBoolean("renderStatus", renderStatus);
        nbt.setDouble("scale", scale);
        nbt.setDouble("rotation", rotation);
        nbt.setBoolean("switch", teRingSwitch);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        renderStatus = nbt.getBoolean("renderStatus");
        rotation = nbt.getDouble("rotation");
        scale = nbt.getDouble("scale");
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
        prevRotation = rotation;
        rotation = (rotation + ROTATION_SPEED) % 360d;
    }

    public double getInterpolatedRotation(float partialTicks) {
        double delta = rotation - prevRotation;

        if (delta < -180.0) delta += 360.0;
        if (delta > 180.0) delta -= 360.0;

        return (prevRotation + delta * partialTicks) % 360.0;
    }
}
