/*
 * ********************************************************************************
 * Copyright (c) 2017 StormyMode, MiningMark48. All Rights Reserved!
 * This file is part of Lightning Additions (MC-Mod).
 *
 * This project cannot be copied and/or distributed without the express
 * permission of StormyMode, MiningMark48 (Developers)!
 * ********************************************************************************
 */
package com.stormy.lightningadditions.item.resource.record;

import javax.annotation.Nonnull;

import com.stormy.lightningadditions.creativetab.CreativeTabLA;
import com.stormy.lightningadditions.reference.ModInformation;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class LARecord extends ItemRecord {

    private final String file;

    public LARecord(String record, SoundEvent sound, String name) {
        super("lightningadditions:" + record, sound);
        setCreativeTab(CreativeTabLA.LA_TAB);
        GameRegistry.register(this, new ResourceLocation(ModInformation.MODID, name));
        setUnlocalizedName(name);
        file = "lightningadditions:music." + record;
    }

    @Nonnull
    @Override
    public String getUnlocalizedNameInefficiently(@Nonnull ItemStack par1ItemStack) {
        return super.getUnlocalizedNameInefficiently(par1ItemStack).replaceAll("item\\.", "item." + ModInformation.PREFIXID);
    }

    @Nonnull
    @Override
    public ResourceLocation getRecordResource(String name) {
        return new ResourceLocation(file);
    }

}