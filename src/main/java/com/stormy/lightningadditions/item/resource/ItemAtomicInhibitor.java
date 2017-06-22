/*
 * ********************************************************************************
 * Copyright (c) 2017 StormyMode, MiningMark48. All Rights Reserved!
 * This file is part of Lightning Additions (MC-Mod).
 *
 * This project cannot be copied and/or distributed without the express
 * permission of StormyMode, MiningMark48 (Developers)!
 * ********************************************************************************
 */

package com.stormy.lightningadditions.item.resource;

import com.stormy.lightningadditions.config.ConfigurationHandler;
import com.stormy.lightningadditions.reference.KeyChecker;
import com.stormy.lightningadditions.reference.Translate;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class ItemAtomicInhibitor extends Item
{

    private Random random = new Random();

    public ItemAtomicInhibitor(){
        setMaxStackSize(1);
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        if (KeyChecker.isHoldingShift()) { par3List.add(Translate.toLocal("tooltip.item.atomic_inhibitor.line1")); }
        else{ par3List.add(Translate.toLocal("tooltip.item.hold") + " " + TextFormatting.AQUA + TextFormatting.ITALIC + Translate.toLocal("tooltip.item.shift")); }
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {

        ItemStack stack = player.getHeldItem(hand);

        if(!world.isRemote){ IBlockState state = world.getBlockState(pos); Block block = state.getBlock(); TileEntity te = world.getTileEntity(pos);
            for (int i = 0; i < (ConfigurationHandler.tickAmount) / (te == null ? 5 : 1); i++) {
                if (te == null)
                {
                    block.updateTick(world, pos, state, this.random);
                }

                else if ((te instanceof ITickable))
                {
                    ((ITickable)te).update();
                }
            }

            if( te instanceof ITickable)
            {
                ((ITickable)te).update();
            }
            stack.damageItem(1, player);
        }
        return EnumActionResult.SUCCESS; }

}
