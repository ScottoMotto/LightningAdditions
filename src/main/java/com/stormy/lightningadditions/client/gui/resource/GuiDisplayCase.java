/*
 *
 *  * ********************************************************************************
 *  * Copyright (c) 2017 StormyMode, MiningMark48. All Rights Reserved!
 *  * This file is part of Lightning Additions (MC-Mod).
 *  *
 *  * This project cannot be copied and/or distributed without the express
 *  * permission of StormyMode, MiningMark48 (Developers)!
 *  * ********************************************************************************
 *
 */

package com.stormy.lightningadditions.client.gui.resource;

import com.stormy.lightningadditions.container.resource.ContainerDisplayCase;
import com.stormy.lightningadditions.init.ModNetworking;
import com.stormy.lightningadditions.network.messages.MessageDisplayCase;
import com.stormy.lightningadditions.reference.ModInformation;
import com.stormy.lightningadditions.tile.resource.TileEntityDisplayCase;
import com.stormy.lightningadditions.utility.GuiUtils;
import com.stormy.lightningadditions.utility.logger.LALogger;
import com.stormy.lightninglib.lib.utils.KeyChecker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class GuiDisplayCase extends GuiContainer{

    private IInventory playerInv;
    private EntityPlayer player;
    private TileEntityDisplayCase te;

    private static String texture = ModInformation.MODID + ":textures/gui/display_case.png";

    private double translateX;
    private double translateY;
    private double translateZ;

    private double rotateX;
    private double rotateY;
    private double rotateZ;

    private double scale;

    public GuiDisplayCase(IInventory playerInv, TileEntityDisplayCase te, EntityPlayer player) {
        super(new ContainerDisplayCase(playerInv, te));

        this.playerInv = playerInv;
        this.player = player;
        this.te = te;

        this.xSize = 176;
        this.ySize = 256;

    }

    @Override
    public void initGui() {
        super.initGui();

        //Translate
        //T - X
        GuiButton buttonTXN = new GuiButton(0, getGuiLeft() + 10, getGuiTop() + 40, 20, 20, "-");
        GuiButton buttonTXP = new GuiButton(1, getGuiLeft() + 60, getGuiTop() + 40, 20, 20, "+");
        //T - Y
        GuiButton buttonTYN = new GuiButton(2, getGuiLeft() + 10, getGuiTop() + 80, 20, 20, "-");
        GuiButton buttonTYP = new GuiButton(3, getGuiLeft() + 60, getGuiTop() + 80, 20, 20, "+");
        //T - Z
        GuiButton buttonTZN = new GuiButton(4, getGuiLeft() + 10, getGuiTop() + 120, 20, 20, "-");
        GuiButton buttonTZP = new GuiButton(5, getGuiLeft() + 60, getGuiTop() + 120, 20, 20, "+");

        //Rotate
        //T - X
        GuiButton buttonRXN = new GuiButton(6, getGuiLeft() + 95, getGuiTop() + 40, 20, 20, "-");
        GuiButton buttonRXP = new GuiButton(7, getGuiLeft() + 145, getGuiTop() + 40, 20, 20, "+");
        //T - Y
        GuiButton buttonRYN = new GuiButton(8, getGuiLeft() + 95, getGuiTop() + 80, 20, 20, "-");
        GuiButton buttonRYP = new GuiButton(9, getGuiLeft() + 145, getGuiTop() + 80, 20, 20, "+");
        //T - Z
        GuiButton buttonRZN = new GuiButton(10, getGuiLeft() + 95, getGuiTop() + 120, 20, 20, "-");
        GuiButton buttonRZP = new GuiButton(11, getGuiLeft() + 145, getGuiTop() + 120, 20, 20, "+");

        //Scale
        GuiButton buttonSN = new GuiButton(12, getGuiLeft() + 95, getGuiTop() + 147, 20, 20, "-");
        GuiButton buttonSP = new GuiButton(13, getGuiLeft() + 145, getGuiTop() + 147, 20, 20, "+");

        //Misc
        GuiButton buttonReset = new GuiButton(14, getGuiLeft() + 10, getGuiTop() + 147, 15, 20, "R");
        GuiButton buttonSave = new GuiButton(15, getGuiLeft() + 28, getGuiTop() + 147, 15, 20, "C");
        GuiButton buttonLoad = new GuiButton(16, getGuiLeft() + 46, getGuiTop() + 147, 15, 20, "P");

        this.buttonList.add(buttonTXP);
        this.buttonList.add(buttonTXN);
        this.buttonList.add(buttonTYP);
        this.buttonList.add(buttonTYN);
        this.buttonList.add(buttonTZP);
        this.buttonList.add(buttonTZN);

        this.buttonList.add(buttonRXP);
        this.buttonList.add(buttonRXN);
        this.buttonList.add(buttonRYP);
        this.buttonList.add(buttonRYN);
        this.buttonList.add(buttonRZP);
        this.buttonList.add(buttonRZN);

        this.buttonList.add(buttonSN);
        this.buttonList.add(buttonSP);

        this.buttonList.add(buttonReset);
        this.buttonList.add(buttonSave);
        this.buttonList.add(buttonLoad);

        this.translateX = te.getTileData().getDouble("tx");
        this.translateY = te.getTileData().getDouble("ty");
        this.translateZ = te.getTileData().getDouble("tz");
        this.rotateX    = te.getTileData().getDouble("rx");
        this.rotateY    = te.getTileData().getDouble("ry");
        this.rotateZ    = te.getTileData().getDouble("rz");

        this.scale      = te.getTileData().getDouble("s");

    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        double transClickAmount = 1D;
        double rotClickAmount = 10D;
        double scaleClickAmount = 1D;

        double transMaxAmount = 25D;
        double rotMaxAmount = 360D;
        double scaleMaxAmount = 5D;

        if (KeyChecker.isHoldingShift()){
            transClickAmount = 0.5D;
            rotClickAmount = 5D;
            scaleClickAmount = 0.5D;
        } else if (KeyChecker.isHoldingCtrl()) {
            transClickAmount = 0.1D;
            rotClickAmount = 1D;
            scaleClickAmount = 0.1D;
        }

        switch (button.id){
            //T - X
            case 0:
                if (translateX > -transMaxAmount) translateX -= transClickAmount;
                break;
            case 1:
                if (translateX < transMaxAmount) translateX += transClickAmount;
                break;
            //T - Y
            case 2:
                if (translateY > -transMaxAmount) translateY -= transClickAmount;
                break;
            case 3:
                if (translateY < transMaxAmount) translateY += transClickAmount;
                break;
            //T - Z
            case 4:
                if (translateZ > -transMaxAmount) translateZ -= transClickAmount;
                break;
            case 5:
                if (translateZ < transMaxAmount) translateZ += transClickAmount;
                break;
            //Rotate
            //R - X
            case 6:
                if (rotateX > -rotMaxAmount) rotateX -= rotClickAmount;
                break;
            case 7:
                if (rotateX < rotMaxAmount) rotateX += rotClickAmount;
                break;
            //R - Y
            case 8:
                if (rotateY > -rotMaxAmount) rotateY -= rotClickAmount;
                break;
            case 9:
                if (rotateY < rotMaxAmount) rotateY += rotClickAmount;
                break;
            //R - Z
            case 10:
                if (rotateZ > -rotMaxAmount) rotateZ -= rotClickAmount;
                break;
            case 11:
                if (rotateZ < rotMaxAmount) rotateZ += rotClickAmount;
                break;
            //Scale
            case 12:
                if (scale > -scaleMaxAmount) scale -= scaleClickAmount;
                break;
            case 13:
                if (scale < scaleMaxAmount) scale += scaleClickAmount;
                break;
            //Reset
            case 14:
                translateX = 0;
                translateY = 0;
                translateZ = 0;
                rotateX = 0;
                rotateY = 0;
                rotateZ = 0;
                scale = 0;
                this.player.sendMessage(new TextComponentString("Values reset."));
                break;
            case 15:
                save();
                break;
            case 16:
                load();
                break;
            default:
                break;
        }

        ModNetworking.INSTANCE.sendToServer(new MessageDisplayCase(this.te.getPos(), this.translateX, this.translateY, this.translateZ, this.rotateX, this.rotateY, this.rotateZ, this.scale));

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1F, 1F, 1F, 1F);
        this.mc.getTextureManager().bindTexture(new ResourceLocation(texture));
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        this.drawTexturedModalRect(getGuiLeft() + 150, getGuiTop() + 6, 177, 1, 15, 15);
    }

    @Override
    protected  void drawGuiContainerForegroundLayer(int mouseX, int mouseY){

        DecimalFormat df = new DecimalFormat("##.##");
        String s = this.te.getDisplayName().getUnformattedComponentText();
        //this.fontRendererObj.drawString(s, 88 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        int x = GuiUtils.getXCenter(s, this.fontRenderer, xSize);
        this.fontRenderer.drawString(s, x, 5, 0x404040);

        //Translate
        //X
        this.fontRenderer.drawString("Translate X", 10, 30, 0x404040);
        this.fontRenderer.drawString(String.valueOf(df.format(translateX)), 37, 45, 0x404040);
        //Y
        this.fontRenderer.drawString("Translate Y", 10, 70, 0x404040);
        this.fontRenderer.drawString(String.valueOf(df.format(translateY)), 37, 85, 0x404040);
        //Z
        this.fontRenderer.drawString("Translate Z", 10, 110, 0x404040);
        this.fontRenderer.drawString(String.valueOf(df.format(translateZ)), 37, 125, 0x404040);

        //Rotate
        //X
        this.fontRenderer.drawString("Rotate X", 110, 30, 0x404040);
        this.fontRenderer.drawString(String.valueOf(df.format(rotateX)), 122, 45, 0x404040);
        //Y
        this.fontRenderer.drawString("Rotate Y", 110, 70, 0x404040);
        this.fontRenderer.drawString(String.valueOf(df.format(rotateY)), 122, 85, 0x404040);
        //Z
        this.fontRenderer.drawString("Rotate Z", 110, 110, 0x404040);
        this.fontRenderer.drawString(String.valueOf(df.format(rotateZ)), 122, 125, 0x404040);

        //Scale
        this.fontRenderer.drawString("Scale", 65, 152, 0x404040);
        this.fontRenderer.drawString(String.valueOf(df.format(scale)), 122, 152, 0x404040);

        renderTooltips(mouseX, mouseY);

//        LALogger.info("TX: " + translateX + ", TY: " + translateY + ", TZ: " + translateZ);

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.func_191948_b(mouseX, mouseY);
    }

    @Override
    public void onGuiClosed() {
        ModNetworking.INSTANCE.sendToServer(new MessageDisplayCase(this.te.getPos(), this.translateX, this.translateY, this.translateZ, this.rotateX, this.rotateY, this.rotateZ, this.scale));

        super.onGuiClosed();
    }

    private void save(){
        try {
            NBTTagCompound tag = this.player.getEntityData();
            NBTTagCompound displayData = new NBTTagCompound();

            displayData.setDouble("tx", translateX);
            displayData.setDouble("ty", translateY);
            displayData.setDouble("tz", translateZ);
            displayData.setDouble("rx", rotateX);
            displayData.setDouble("ry", rotateY);
            displayData.setDouble("rz", rotateZ);
            displayData.setDouble("s", scale);

            tag.setTag("display_case_data", displayData);

            this.player.sendMessage(new TextComponentString("Values copied."));
        } catch (NullPointerException e){
            this.player.sendMessage(new TextComponentString("Error occurred while saving data."));
        }
    }

    private void load(){
        try {
            NBTTagCompound tag = this.player.getEntityData();
            NBTTagCompound displayData = tag.getCompoundTag("display_case_data");

            this.translateX = displayData.getDouble("tx");
            this.translateY = displayData.getDouble("ty");
            this.translateZ = displayData.getDouble("tz");
            this.rotateX = displayData.getDouble("rx");
            this.rotateY = displayData.getDouble("ry");
            this.rotateZ = displayData.getDouble("rz");
            this.scale = displayData.getDouble("s");

            this.player.sendMessage(new TextComponentString("Values pasted."));
        } catch (NullPointerException e){
            this.player.sendMessage(new TextComponentString("Error occurred while loading data."));
        }
    }

    private void renderTooltips(int mouseX, int mouseY){
        Minecraft mc = Minecraft.getMinecraft();
        if (this.isMouseOver(mouseX, mouseY, 10, 147, 25, 167)){
            List<String> text = new ArrayList<String>();
            text.add("Reset");
            net.minecraftforge.fml.client.config.GuiUtils.drawHoveringText(text, mouseX - ((this.width - this.xSize) / 2), mouseY - ((this.height - this.ySize) / 2), mc.displayWidth, mc.displayHeight, -1, mc.fontRenderer);
        } else if (this.isMouseOver(mouseX, mouseY, 28, 147, 43, 167)){
            List<String> text = new ArrayList<String>();
            text.add("Copy");
            net.minecraftforge.fml.client.config.GuiUtils.drawHoveringText(text, mouseX - ((this.width - this.xSize) / 2), mouseY - ((this.height - this.ySize) / 2), mc.displayWidth, mc.displayHeight, -1, mc.fontRenderer);
        } else if (this.isMouseOver(mouseX, mouseY, 46, 147, 61, 167)){
            List<String> text = new ArrayList<String>();
            text.add("Paste");
            net.minecraftforge.fml.client.config.GuiUtils.drawHoveringText(text, mouseX - ((this.width - this.xSize) / 2), mouseY - ((this.height - this.ySize) / 2), mc.displayWidth, mc.displayHeight, -1, mc.fontRenderer);
        } else if (this.isMouseOver(mouseX, mouseY, 150, 6, 165, 21)){
            List<String> text = new ArrayList<String>();
            text.add(TextFormatting.BOLD + "" + TextFormatting.UNDERLINE + "Info");
            text.add("Put item to display in the slot at the top left.");
            text.add("");
            text.add("Translate moves the item along an axis.");
            text.add("Rotate spins the item along an axis.");
            text.add("Scale changes the size of the item.");
            text.add("");
            text.add("Click 'Reset' (R) to reset the settings.");
            text.add("Click 'Copy' (C) to copy settings to the clipboard.");
            text.add("Click 'Paste' (P) to paste setting from the clipboard.");
            text.add("");
            text.add(TextFormatting.UNDERLINE + "Keyboard Control");
            text.add("Shift - Half Increments");
            text.add("Ctrl - Single Increments");
            net.minecraftforge.fml.client.config.GuiUtils.drawHoveringText(text, mouseX - ((this.width - this.xSize) / 2), mouseY - ((this.height - this.ySize) / 2), mc.displayWidth, mc.displayHeight, -1, mc.fontRenderer);
        }
    }

    private boolean isMouseOver(int mouseX, int mouseY, int minX, int minY, int maxX, int maxY){
        int actualX = mouseX - ((this.width - this.xSize) / 2);
        int actualY = mouseY - ((this.height - this.ySize) / 2);
        return (actualX >= minX) && (actualX <= maxX) && (actualY >= minY) && (actualY <= maxY);
    }

}
