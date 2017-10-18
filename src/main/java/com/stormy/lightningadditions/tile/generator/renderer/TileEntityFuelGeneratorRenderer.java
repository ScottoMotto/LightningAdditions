package com.stormy.lightningadditions.tile.generator.renderer;

import com.stormy.lightningadditions.tile.generator.TileEntityFuelGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class TileEntityFuelGeneratorRenderer extends TileEntitySpecialRenderer<TileEntityFuelGenerator>{

    private EntityItem entityItem = new EntityItem(Minecraft.getMinecraft().world, 0D, 0D, 0D);

    @Override
    public void func_192841_a(TileEntityFuelGenerator te, double x, double y, double z, float partialTicks, int destroyStage, float partial) {
        if (te != null){
            ItemStack fuelStack = te.getStackInSlot(0).copy();
            if (!fuelStack.isEmpty()){
                fuelStack.setCount(1);
                entityItem.setEntityItemStack(fuelStack);
                entityItem.hoverStart = 0;

                if (fuelStack.getItem() instanceof ItemBlock) {
                    GlStateManager.pushMatrix();

                    GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);

                    double xOffset = 0D;
                    double yOffset = -0.35D;
                    double zOffset = 0D;

                    GlStateManager.scale(1.5D, 1.5D, 1.5D);
                    GlStateManager.translate(xOffset, yOffset, zOffset);

                    Minecraft.getMinecraft().getRenderManager().doRenderEntity(entityItem, 0D, 0D, 0D, 0.0F, 0.0F, false);

                    GlStateManager.popMatrix();
                } else {
                    GlStateManager.pushMatrix();

                    GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);

                    double xOffset = 0D;
                    double yOffset = -0.175D;
                    double zOffset = -0.5D;

                    GlStateManager.translate(xOffset, yOffset, zOffset);

                    GlStateManager.rotate(90, 1f, 0f, 0f);

                    Minecraft.getMinecraft().getRenderManager().doRenderEntity(entityItem, 0D, 0D, 0D, 0.0F, 0.0F, false);

                    GlStateManager.popMatrix();

                }
            }
        }
    }

}
