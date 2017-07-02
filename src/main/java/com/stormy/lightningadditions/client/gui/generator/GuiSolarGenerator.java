package com.stormy.lightningadditions.client.gui.generator;

import com.stormy.lightningadditions.client.container.generator.ContainerSolarGenerator;
import com.stormy.lightningadditions.init.ModItems;
import com.stormy.lightningadditions.reference.ModInformation;
import com.stormy.lightningadditions.tile.generator.TileEntitySolarGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class GuiSolarGenerator extends GuiContainer{

    private TileEntitySolarGenerator te;
    private IInventory playerInv;

    public GuiSolarGenerator(IInventory playerInv, TileEntitySolarGenerator te) {
        super(new ContainerSolarGenerator(playerInv, te));

        this.te = te;
        this.playerInv = playerInv;

        this.xSize = 176;
        this.ySize = 168;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(new ResourceLocation(ModInformation.MODID + ":textures/gui/generator/gui_solar_generator.png"));
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

        //Energy Bar
        this.mc.getTextureManager().bindTexture(new ResourceLocation(ModInformation.MODID + ":textures/gui/generator/gui_solar_generator.png"));
        this.drawTexturedModalRect(8, 66 - getProgressLevel(51), 180, 21, 21, getProgressLevel(51));

        //Progress
        this.mc.getTextureManager().bindTexture(new ResourceLocation(ModInformation.MODID + ":textures/gui/generator/gui_solar_generator.png"));
        this.drawTexturedModalRect(40, 46 - getCooldownLevel(14), 180, 74, 12, getCooldownLevel(15));

        String s = this.te.getDisplayName().getUnformattedText();
        this.fontRendererObj.drawString(s, 88 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        this.fontRendererObj.drawString(this.playerInv.getDisplayName().getUnformattedText(), 8, 75, 4210752);

        if (this.te.getField(0) >= this.te.getField(1)){
            this.fontRendererObj.drawString("Storage Full", 67, 19, 4210752);
        }else if(this.te.getField(0) < this.te.getField(1)) {
            NumberFormat format = NumberFormat.getInstance();
            this.fontRendererObj.drawString("RF/t: " + format.format(this.te.getField(3)), 67, 19, 4210752);
        }

//        Debug Code
//        int actualX = mouseX - ((this.width - this.xSize) / 2);
//        int actualY = mouseY - ((this.height - this.ySize) / 2);
//        ModLogger.info(actualX + ", " + actualY);

        if (this.isMouseOver(mouseX, mouseY, 7, 14, 29, 66)){
            Minecraft mc = Minecraft.getMinecraft();

            List<String> text = new ArrayList<String>();
            text.add(this.getOverlayText());
            net.minecraftforge.fml.client.config.GuiUtils.drawHoveringText(text, mouseX - ((this.width - this.xSize) / 2), mouseY - ((this.height - this.ySize) / 2), mc.displayWidth, mc.displayHeight, -1, mc.fontRendererObj);
        }

    }

    private boolean isMouseOver(int mouseX, int mouseY, int minX, int minY, int maxX, int maxY){
        int actualX = mouseX - ((this.width - this.xSize) / 2);
        int actualY = mouseY - ((this.height - this.ySize) / 2);
        return (actualX >= minX) && (actualX <= maxX) && (actualY >= minY) && (actualY <= maxY);
    }

    private String getOverlayText(){
        NumberFormat format = NumberFormat.getInstance();
        return String.format("%s/%s RF", format.format(this.te.getField(0)), format.format(this.te.getField(1)));
    }

    private int getProgressLevel(int pixels) {
        int rf = this.te.getField(0);
        int maxRF = this.te.getField(1);
        return maxRF != 0 && rf != 0 ? (rf * pixels) / maxRF : 0;

    }

    private int getCooldownLevel(int pixels) {
        int cooldown = this.te.getField(2);
        int maxCooldown = this.te.cooldownMax;
        return maxCooldown != 0 && cooldown != 0 ? (cooldown * pixels) / maxCooldown : 0;

    }

}
