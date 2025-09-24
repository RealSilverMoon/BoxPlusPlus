package com.silvermoon.boxplusplus.common.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;

import cpw.mods.fml.relauncher.Side;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAdvanced;
import micdoodle8.mods.galacticraft.core.util.Annotations;

public class TEBoxRing extends TileEntityAdvanced {

    @Annotations.NetworkedField(targetSide = Side.CLIENT)
    public float scale = 1;

    @Annotations.NetworkedField(targetSide = Side.CLIENT)
    public float rotation = 0;

    @Annotations.NetworkedField(targetSide = Side.CLIENT)
    public float prevRotation = 0;

    @Annotations.NetworkedField(targetSide = Side.CLIENT)
    public boolean renderStatus = false;

    @Annotations.NetworkedField(targetSide = Side.CLIENT)
    public boolean teRingSwitch = true;

    @Annotations.NetworkedField(targetSide = Side.CLIENT)
    public int count = 1;

    private static float ROTATION_SPEED = 1.2f;

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
        nbt.setFloat("scale", scale);
        nbt.setFloat("rotation", rotation);
        nbt.setBoolean("switch", teRingSwitch);
        nbt.setInteger("count", count);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        renderStatus = nbt.getBoolean("renderStatus");
        rotation = nbt.getFloat("rotation");
        scale = nbt.getFloat("scale");
        teRingSwitch = nbt.getBoolean("switch");
        count = nbt.getInteger("count");
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
        rotation = (rotation + ROTATION_SPEED) % 360f;
    }

    public float getInterpolatedRotation(float partialTicks) {
        float delta = MathHelper.wrapAngleTo180_float(rotation - prevRotation);
        return prevRotation + delta * partialTicks;
    }

}
