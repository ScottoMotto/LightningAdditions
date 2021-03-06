/*
 * ********************************************************************************
 * Copyright (c) 2017 StormyMode, MiningMark48. All Rights Reserved!
 * This file is part of Lightning Additions (MC-Mod).
 *
 * This project cannot be copied and/or distributed without the express
 * permission of StormyMode, MiningMark48 (Developers)!
 * ********************************************************************************
 */

package com.stormy.lightningadditions.block.base;

import com.stormy.lightningadditions.creativetab.CreativeTabLA;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class BlockContainerLA extends BlockContainer
{
    public BlockContainerLA(Material materialIn, String s) {
        super(materialIn);
        this.setUnlocalizedName(s);
        this.setCreativeTab(CreativeTabLA.LA_TAB);
        this.setRegistryName(s);
        GameRegistry.registerTileEntity(tile(), s);
    }

    protected abstract Class<? extends TileEntity> tile();

    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }



}
