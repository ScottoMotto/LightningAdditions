package com.stormy.lightningadditions.tile.resource.renderer;

import com.stormy.lightningadditions.tile.resource.TileEntityParticleAccelerator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;

public class TileEntityParticlerAcceleratorRenderer extends TileEntitySpecialRenderer<TileEntityParticleAccelerator> {

    private EntityItem entityItem = new EntityItem(Minecraft.getMinecraft().world, 0D, 0D, 0D);

    @Override
    public void func_192841_a(TileEntityParticleAccelerator te, double x, double y, double z, float partialTicks, int destroyStage, float partial) {
        if (te != null){
            ItemStack stack = te.getStackInSlot(1).copy();
            if (!stack.isEmpty()){
                stack.setCount(1);
                entityItem.setEntityItemStack(stack);
                entityItem.hoverStart = 0;

                GlStateManager.pushMatrix();

                GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);

                GlStateManager.scale(0.65D, 0.65D, 0.65D);

                double xOffset = -0.1D;
                double yOffset = 0.25D;
                double zOffset = -0.6D;

                GlStateManager.translate(xOffset, yOffset, zOffset);

                GlStateManager.rotate(90, 1f, 0f, 0f);
                GlStateManager.rotate(180, 0f, 1f, 0f);

                Minecraft.getMinecraft().getRenderManager().doRenderEntity(entityItem, 0D, 0D, 0D, 0.0F, 0.0F, false);

                GlStateManager.popMatrix();

            }
        }
    }

}
