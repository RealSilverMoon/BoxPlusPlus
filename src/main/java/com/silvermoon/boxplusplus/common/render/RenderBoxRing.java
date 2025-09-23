package com.silvermoon.boxplusplus.common.render;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import com.silvermoon.boxplusplus.Tags;
import com.silvermoon.boxplusplus.common.tileentities.TEBoxRing;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBoxRing extends TileEntitySpecialRenderer {

    private static final ResourceLocation BoxRingTexture = new ResourceLocation(Tags.MODID, "textures/models/ring.png");
    private static final IModelCustom Ring = AdvancedModelLoader
        .loadModel(new ResourceLocation(Tags.MODID, "models/Ring.obj"));

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        if (!(tile instanceof TEBoxRing ring)) return;

        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);

        double rotation = ring.getInterpolatedRotation(partialTicks);

        if (ring.teRingSwitch && ring.renderStatus) {
            GL11.glRotated(rotation, 1, 1, 1);
            renderRing((float) (1.1f * ring.scale));
            GL11.glRotated(rotation, 0, 0, 1);
            renderRing((float) (1.4f * ring.scale));
        }

        GL11.glRotated(rotation, 0, -1, 1);
        renderRing(0.0118f);

        GL11.glPopMatrix();
    }

    private void renderRing(float scale) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        this.bindTexture(BoxRingTexture);
        GL11.glScaled(scale, scale, scale);

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
        Ring.renderAll();

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }
}
