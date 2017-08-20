/*
 * ********************************************************************************
 * Copyright (c) 2017 StormyMode, MiningMark48. All Rights Reserved!
 * This file is part of Lightning Additions (MC-Mod).
 *
 * This project cannot be copied and/or distributed without the express
 * permission of StormyMode, MiningMark48 (Developers)!
 * ********************************************************************************
 */

package com.stormy.lightningadditions.proxy;

import com.stormy.lightningadditions.init.ModBlocks;
import com.stormy.lightningadditions.init.ModItems;
import com.stormy.lightningadditions.init.ModKeys;
import com.stormy.lightningadditions.reference.ModInformation;
import com.stormy.lightningadditions.tile.resource.TileEntitySky;
import com.stormy.lightningadditions.tile.resource.TileEntitySkyRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerModel(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(ModInformation.MODID + ":" + item.getUnlocalizedName().substring(5), "inventory"));
    }

    public void preInit(FMLPreInitializationEvent event)
    {

    }

    public void init(FMLInitializationEvent event) {
    }


    public void postInit(FMLPostInitializationEvent event) {
        Minecraft.getMinecraft().getFramebuffer().enableStencil();

        ModKeys.init();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerRenders() {
        ModItems.registerRenders();
        ModBlocks.registerRenders();

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySky.class, new TileEntitySkyRenderer());

    }

    @Override
    @SideOnly(Side.CLIENT)
    public World getClientWorld() {
        return FMLClientHandler.instance().getClient().world;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EntityPlayer getClientPlayer() {
        return Minecraft.getMinecraft().player;
    }

}
