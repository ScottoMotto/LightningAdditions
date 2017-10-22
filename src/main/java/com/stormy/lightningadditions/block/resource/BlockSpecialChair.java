package com.stormy.lightningadditions.block.resource;

import com.stormy.lightninglib.lib.block.BlockBase;
import com.stormy.lightninglib.lib.utils.KeyChecker;
import com.stormy.lightninglib.lib.utils.TranslateUtils;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

public class BlockSpecialChair extends BlockBase
{
    public BlockSpecialChair() {
        super(Material.WOOD);
        setSoundType(SoundType.WOOD);
        setHardness(1.0F);
        setResistance(0.75F);
    }

    public static class ForgeEventHandler
    {
        @SubscribeEvent
        public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event)
        {
            if(!event.getWorld().isRemote)
            {
                World w = event.getWorld();
                BlockPos p = event.getPos();
                IBlockState s = w.getBlockState(p);
                Block b = w.getBlockState(p).getBlock();
                EntityPlayer e = event.getEntityPlayer();

                if((b instanceof BlockSpecialChair) && !EntitySitting.OCCUPIED.containsKey(p) && e.getHeldItemMainhand() == ItemStack.EMPTY)
                {
                    EntitySitting sit = new EntitySitting(w, p);
                    w.spawnEntity(sit);
                    e.startRiding(sit); } }
        }

        @SubscribeEvent
        public void onBreak(BlockEvent.BreakEvent event)
        {
            if(EntitySitting.OCCUPIED.containsKey(event.getPos()))
            {
                EntitySitting.OCCUPIED.get(event.getPos()).setDead();
                EntitySitting.OCCUPIED.remove(event.getPos());
            }
        }

        @SubscribeEvent
        public void onEntityMount(EntityMountEvent event)
        {
            if(event.isDismounting())
            {
                Entity e = event.getEntityBeingMounted();

                if(e instanceof EntitySitting)
                {
                    e.setDead();
                    EntitySitting.OCCUPIED.remove(e.getPosition());
                }
            }
        }
    }


    public static class EntitySitting extends Entity
    { public static final HashMap<BlockPos,EntitySitting> OCCUPIED = new HashMap<BlockPos,EntitySitting>();

        public EntitySitting(World world)
        { super(world);
            noClip = true;
            height = 0.0001F;
            width = 0.0001F; }

        public EntitySitting(World world, BlockPos pos)
        { super(world);
            setPosition(pos.getX() + 0.5D, pos.getY() + 0.48D, pos.getZ() + 0.5D);
            noClip = true;
            height = 0.0001F;
            width = 0.0001F;
            OCCUPIED.put(pos, this); }

        @Override
        protected void entityInit()
        {}
        @Override
        protected void readEntityFromNBT(NBTTagCompound compound)
        {}
        @Override
        protected void writeEntityToNBT(NBTTagCompound compound)
        {}
    }



    @Nonnull
    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState state)
    { return false; }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings("deprecation")
    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    { return false; }

    //Custom Tooltip
    @Override
    public void addInformation(ItemStack par1ItemStack, @Nullable World world, List par3List, ITooltipFlag par4) {
        if (KeyChecker.isHoldingShift()) {
            par3List.add(TextFormatting.AQUA + TranslateUtils.toLocal("tooltip.block.special_chair.line1"));
        } else {
            par3List.add(TranslateUtils.toLocal("tooltip.item.hold") + " " + TextFormatting.AQUA + TextFormatting.ITALIC + TranslateUtils.toLocal("tooltip.item.shift"));
        }
    }
}
