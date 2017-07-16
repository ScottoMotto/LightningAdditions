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

package com.stormy.lightningadditions.block.generator;

import com.stormy.lightningadditions.LightningAdditions;
import com.stormy.lightningadditions.network.GuiHandler;
import com.stormy.lightningadditions.tile.generator.TileEntityBioFuelGenerator;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockBioFuelGenerator extends BlockBaseGenerator{

    private static final AxisAlignedBB BOUNDING_BOX_ON = new AxisAlignedBB(0.0, 0.0D, 0.0D, 1.0D, 0.8125D, 1.0D);
    private static final AxisAlignedBB BOUNDING_BOX_OFF = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);

    public BlockBioFuelGenerator() {
        super(Material.ROCK, BOUNDING_BOX_ON, BOUNDING_BOX_OFF);
        setHardness(1.0f);
        setResistance(0.5f);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityBioFuelGenerator();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote){
            if (playerIn != null) {
                playerIn.openGui(LightningAdditions.INSTANCE, GuiHandler.gui_id_biofuel_generator, worldIn, pos.getX(), pos.getY(), pos.getZ());
            }
        }
        return true;
    }

    @Override
    public void setState(World worldIn, BlockPos pos, boolean isActive)
    {
        super.setState(worldIn, pos, isActive);
    }

}
