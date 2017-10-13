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
import com.stormy.lightningadditions.reference.ModInformation;
import com.stormy.lightningadditions.tile.resource.TileEntityDisplayCase;
import com.stormy.lightningadditions.utility.GuiUtils;
import com.stormy.lightninglib.lib.utils.KeyChecker;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.text.DecimalFormat;

public class GuiDisplayCase extends GuiContainer{

    private IInventory playerInv;
    private TileEntityDisplayCase te;

    private static String texture = ModInformation.MODID + ":textures/gui/display_case.png";

    private double translateX;
    private double translateY;
    private double translateZ;

    private double rotateX;
    private double rotateY;
    private double rotateZ;

    private double scale;

    public GuiDisplayCase(IInventory playerInv, TileEntityDisplayCase te) {
        super(new ContainerDisplayCase(playerInv, te));

        this.playerInv = playerInv;
        this.te = te;

        this.xSize = 176;
        this.ySize = 256;

        this.translateX = te.getVar(0);
        this.translateY = te.getVar(1);
        this.translateZ = te.getVar(2);
        this.rotateX = te.getVar(3);
        this.rotateY = te.getVar(4);
        this.rotateZ = te.getVar(5);

        this.scale = te.getVar(6);

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

        //Reset to Default
        GuiButton buttonReset = new GuiButton(14, getGuiLeft() + 10, getGuiTop() + 147, 50, 20, "Reset");

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
            case 14:
                translateX = 0;
                translateY = 0;
                translateZ = 0;
                rotateX = 0;
                rotateY = 0;
                rotateZ = 0;
                scale = 0;
                break;
            default:
                break;
        }

        this.te.setVar(0, translateX);
        this.te.setVar(1, translateY);
        this.te.setVar(2, translateZ);
        this.te.setVar(3, rotateX);
        this.te.setVar(4, rotateY);
        this.te.setVar(5, rotateZ);
        this.te.setVar(6, scale);

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1F, 1F, 1F, 1F);
        this.mc.getTextureManager().bindTexture(new ResourceLocation(texture));
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
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

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.func_191948_b(mouseX, mouseY);
    }

}
