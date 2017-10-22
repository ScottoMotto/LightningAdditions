package com.stormy.lightningadditions.item.resource;

import java.util.List;
import java.util.Random;

import com.stormy.lightningadditions.reference.ModInformation;
import com.stormy.lightninglib.lib.item.IItemPropertyGetterFix;
import com.stormy.lightninglib.lib.utils.KeyChecker;
import com.stormy.lightninglib.lib.utils.TranslateUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockReed;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

import static com.stormy.lightninglib.lib.utils.StringHelper.BOLD;
import static com.stormy.lightninglib.lib.utils.StringHelper.ITALIC;

public class ItemWateringCan extends Item {

    int effectiveness = 75;
    private static BlockPos posBlockWatering = null;
    private static EntityPlayer playerWatering = null;
    private static final String taggedWateringCan = "watering.can";
    private static final String taggedWateringCanLocked = "watering.can.locked";
    private static final String taggedWateringCanTimer = "watering.can.timer";

    public ItemWateringCan(String name) {
        this.maxStackSize = 1;
        setMaxDamage(15);
        if (this.effectiveness < 10) {
            this.effectiveness = 10;
        }
        if (this.effectiveness > 100) {
            this.effectiveness = 100;
        }

        this.addPropertyOverride(new ResourceLocation(ModInformation.MODID, "water"),
                IItemPropertyGetterFix.create((stack, worldIn, entityIn) -> {
                    if (entityIn != null && !stack.isEmpty() && stack.getItem() instanceof ItemWateringCan && entityIn.getItemInUseCount() > 0) return 1.0F;
                    else return 0.0F;
                }));
    }

    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemStack = player.getHeldItem(hand);
        if (!player.canPlayerEdit(pos.offset(facing), facing, itemStack)) {
            return EnumActionResult.PASS;
        }
        if ((!world.isRemote) && (player.isSneaking()) && (!getNBTTagBoolean(itemStack, "watering.can.locked"))) {
            updateNBTData(itemStack, "watering.can.locked", true);
        } else if ((!world.isRemote) && (player.isSneaking()) && (getNBTTagBoolean(itemStack, "watering.can.locked"))) {
            updateNBTData(itemStack, "watering.can.locked", false);
        }
        if (!getNBTTagBoolean(itemStack, "watering.can.locked")) {
            player.setActiveHand(hand);
            doWater(itemStack, world, player, pos);
        }
        return EnumActionResult.PASS;
    }

    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if ((entity instanceof EntityPlayer)) {
            boolean offhand = itemSlot == EntityEquipmentSlot.OFFHAND.getSlotIndex();
            if (isSelected || offhand) {
                if (getNBTTagBoolean(stack, "watering.can.locked")) {
                    if (getNBTTagInt(stack, "watering.can.timer") >= 4) {
                        updateNBTDataInt(stack, "watering.can.timer", 0);
                        EntityPlayer player = (EntityPlayer) entity;
                        RayTraceResult rtr = rayTrace(world, player, false);
                        if (rtr.typeOfHit == RayTraceResult.Type.BLOCK) {
                            posBlockWatering = rtr.getBlockPos();
                            playerWatering = (EntityPlayer) entity;
                            player.setActiveHand(offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
                            doWater(stack, world, playerWatering, posBlockWatering);
                        } else {
                            posBlockWatering = null;
                            playerWatering = null;
                        }
                    } else {
                        updateNBTDataInt(stack, "watering.can.timer", getNBTTagInt(stack, "watering.can.timer") + 1);
                    }
                }
            } else {
                updateNBTData(stack, "watering.can.locked", false);
                updateNBTDataInt(stack, "watering.can.timer", 0);
            }
        }
    }

    public void doWater(ItemStack stack, World world, EntityPlayer player, BlockPos pos) {
        Random rand = new Random();
        for (int a = -1; a <= 1; a++) {
            for (int b = -1; b <= 1; b++) {
                double d0 = pos.add(a, 0, b).getX() + rand.nextFloat();
                double d1 = pos.add(a, 0, b).getY() + 1.0D;
                double d2 = pos.add(a, 0, b).getZ() + rand.nextFloat();

                IBlockState checkSolidState = world.getBlockState(pos);
                Block checkSolid = world.getBlockState(pos).getBlock();
                if ((checkSolid.isFullCube(checkSolidState)) || ((checkSolid instanceof BlockFarmland))) {
                    d1 += 1.0D;
                }
                world.spawnParticle(EnumParticleTypes.WATER_DROP, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[5]);
            }
        }
        if (!world.isRemote) {
            int chance = Stuff.randInt(1, 100);
            if (chance <= this.effectiveness) {
                for (int xAxis = -1; xAxis <= 1; xAxis++) {
                    for (int zAxis = -1; zAxis <= 1; zAxis++) {
                        for (int yAxis = -1; yAxis <= 1; yAxis++) {
                            Block checkBlock = world.getBlockState(pos.add(xAxis, yAxis, zAxis)).getBlock();
                            if (((checkBlock instanceof IGrowable)) || ((checkBlock instanceof BlockBush)) || ((checkBlock instanceof BlockCactus)) || ((checkBlock instanceof BlockReed))) {
                                world.scheduleBlockUpdate(pos.add(xAxis, yAxis, zAxis), checkBlock, 0, 1);
                            }
                        }
                    }
                }
            }
        }
    }

    public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
        createNBTData(itemStack);
    }

    private static void createNBTData(ItemStack itemStack) {
        NBTTagCompound data = new NBTTagCompound();
        data.setBoolean("watering.can.locked", false);
        data.setInteger("watering.can.timer", 0);
        itemStack.setTagInfo("watering.can", data);
    }

    private static void updateNBTData(ItemStack itemStack, String tag, boolean bool) {
        if (itemStack.getTagCompound() != null) {
            NBTTagCompound data = itemStack.getTagCompound().getCompoundTag("watering.can");
            data.setBoolean(tag, bool);
            itemStack.setTagInfo("watering.can", data);
        } else {
            createNBTData(itemStack);
        }
    }

    private static void updateNBTDataInt(ItemStack itemStack, String tag, int value) {
        if (itemStack.getTagCompound() != null) {
            NBTTagCompound data = itemStack.getTagCompound().getCompoundTag("watering.can");
            data.setInteger(tag, value);
            itemStack.setTagInfo("watering.can", data);
        } else {
            createNBTData(itemStack);
        }
    }

    private static boolean getNBTTagBoolean(ItemStack itemStack, String tag) {
        if (itemStack.getTagCompound() != null) {
            NBTTagCompound data = itemStack.getTagCompound().getCompoundTag("watering.can");
            return data.getBoolean(tag);
        }
        return false;
    }

    private static int getNBTTagInt(ItemStack itemStack, String tag) {
        if (itemStack.getTagCompound() != null) {
            NBTTagCompound data = itemStack.getTagCompound().getCompoundTag("watering.can");
            return data.getInteger(tag);
        }
        return 0;
    }

    private static class Stuff {
        public static int randInt(int min, int max) {
            Random rand = new Random();
            int randomNum = rand.nextInt(max - min + 1) + min;
            return randomNum;
        }
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        if ((getNBTTagBoolean(oldStack, "watering.can.locked") != getNBTTagBoolean(newStack, "watering.can.locked")) || (slotChanged) || (oldStack.getItem() != newStack.getItem())) {
            return true;
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return getNBTTagBoolean(stack, "watering.can.locked");
    }

    //Custom Tooltip
    @Override
    public void addInformation(ItemStack par1ItemStack, @Nullable World world, List par3List, ITooltipFlag par4) {
        if (KeyChecker.isHoldingShift()) { par3List.add(TextFormatting.AQUA + TranslateUtils.toLocal("tooltip.item.watering_can.line1")); }
        if (KeyChecker.isHoldingShift()) { par3List.add(TextFormatting.LIGHT_PURPLE + BOLD + TranslateUtils.toLocal("tooltip.item.watering_can.line2")); }
        else { par3List.add(TranslateUtils.toLocal("tooltip.item.hold") + " " + TextFormatting.AQUA + TextFormatting.ITALIC + TranslateUtils.toLocal("tooltip.item.shift")); }
    }
}

