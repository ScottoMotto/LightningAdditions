package com.stormy.lightningadditions.tile.resource.renderer;

import com.stormy.lightningadditions.tile.resource.TileEntityDisplayCase;
import com.stormy.lightningadditions.tile.resource.TileEntityParticleAccelerator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class TileEntityDisplayCaseRenderer extends TileEntitySpecialRenderer<TileEntityDisplayCase> {

    private EntityItem entityItem = new EntityItem(Minecraft.getMinecraft().world, 0D, 0D, 0D);

    @Override
    public void func_192841_a(TileEntityDisplayCase te, double x, double y, double z, float partialTicks, int destroyStage, float partial) {
        if (te != null){
            ItemStack stack = te.getStackInSlot(0).copy();
            if (!stack.isEmpty()){
                stack.setCount(1);
                entityItem.setEntityItemStack(stack);
                entityItem.hoverStart = 0;

                if (!(stack.getItem() instanceof ItemBlock)) {
                    GlStateManager.pushMatrix();

                    GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);

                    double xOffset = 0D;
                    double yOffset = -0.475D;
                    double zOffset = 0D;

                    GlStateManager.translate(xOffset, yOffset, zOffset);

                    Minecraft.getMinecraft().getRenderManager().doRenderEntity(entityItem, 0D, 0D, 0D, 0.0F, 0.0F, false);

                    GlStateManager.popMatrix();
                } else {
                    GlStateManager.pushMatrix();

                    GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);

                    GlStateManager.scale(1.5D, 1.5D, 1.5D);

                    double xOffset = 0.2D;
                    double yOffset = -0.2D;
                    double zOffset = -0.2D;

                    GlStateManager.translate(xOffset, yOffset, zOffset);

                    GlStateManager.rotate(45f, 1, 0, 1);

                    Minecraft.getMinecraft().getRenderManager().doRenderEntity(entityItem, 0D, 0D, 0D, 0.0F, 0.0F, false);

                    GlStateManager.popMatrix();
                }

            }
        }
    }

}
