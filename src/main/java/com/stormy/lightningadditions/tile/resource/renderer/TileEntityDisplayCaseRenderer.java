package com.stormy.lightningadditions.tile.resource.renderer;

import com.stormy.lightningadditions.tile.resource.TileEntityDisplayCase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
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

                    double xOffset = 0D      + te.getVar(0)/50;
                    double yOffset = -0.475D + te.getVar(1)/50;
                    double zOffset = 0D      + te.getVar(2)/50;

                    GlStateManager.translate(xOffset, yOffset, zOffset);

                    double xRotate = te.getVar(3);
                    double yRotate = te.getVar(4);
                    double zRotate = te.getVar(5);

                    GlStateManager.rotate((float) xRotate, 1, 0,0);
                    GlStateManager.rotate((float) yRotate, 0, 1,0);
                    GlStateManager.rotate((float) zRotate, 0, 0,1);

                    GlStateManager.scale(1 + (float) te.getVar(6)/10, 1 + (float) te.getVar(6)/10, 1 + (float) te.getVar(6)/10);

                    Minecraft.getMinecraft().getRenderManager().doRenderEntity(entityItem, 0D, 0D, 0D, 0.0F, 0.0F, false);

                    GlStateManager.popMatrix();
                } else {
                    GlStateManager.pushMatrix();

                    GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);

                    GlStateManager.scale(1.5D, 1.5D, 1.5D);

                    double xOffset = 0.2D  + te.getVar(0)/50;
                    double yOffset = -0.2D + te.getVar(1)/50;
                    double zOffset = -0.2D + te.getVar(2)/50;

                    GlStateManager.translate(xOffset, yOffset, zOffset);

                    GlStateManager.rotate(45f, 1, 0, 1);

                    double xRotate = te.getVar(3);
                    double yRotate = te.getVar(4);
                    double zRotate = te.getVar(5);

                    GlStateManager.rotate((float) xRotate, 1, 0,0);
                    GlStateManager.rotate((float) yRotate, 0, 1,0);
                    GlStateManager.rotate((float) zRotate, 0, 0,1);

                    GlStateManager.scale(1 + (float) te.getVar(6)/10, 1 + (float) te.getVar(6)/10, 1 + (float) te.getVar(6)/10);

                    Minecraft.getMinecraft().getRenderManager().doRenderEntity(entityItem, 0D, 0D, 0D, 0.0F, 0.0F, false);

                    GlStateManager.popMatrix();
                }

            }
        }
    }

}
