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

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.*;

public abstract class CommonProxy
{


    public void preInit(FMLPreInitializationEvent event){ }


    public void registerModel(Item item){

    }

    public void registerRenders(){

    }

    public void init(FMLInitializationEvent event){ }

    public World getClientWorld() {
        return null;
    }
    public EntityPlayer getClientPlayer() {
        return null;
    }

    public void registerModels()
    {

    }

    public void postInit(FMLPostInitializationEvent event){ }

    public ModelBiped getArmorModel() {
        return null;
    }

}
