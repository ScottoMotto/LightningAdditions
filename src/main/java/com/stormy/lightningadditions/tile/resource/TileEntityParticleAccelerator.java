/*
 * *******************************************************************************
 * Copyright (c) 2017 StormyMode, MiningMark48. All Rights Reserved!
 * This file is part of Lightning Additions (MC-Mod).
 *
 * This project cannot be copied and/or distributed without the express
 * permission of StormyMode, MiningMark48 (Developers)!
 * *******************************************************************************
 */

package com.stormy.lightningadditions.tile.resource;

import com.stormy.lightningadditions.crafting.RegistryParticleAccelerator;
import com.stormy.lightningadditions.tile.base.LATile;
import com.stormy.lightningadditions.utility.logger.LALogger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;

/**
 * Created by KitsuneAlex & MiningMark48
 */
public class TileEntityParticleAccelerator extends LATile implements ISidedInventory, IInventory {

    private int defaultCooldown = 100;
    private int cooldown = defaultCooldown;
    private double progress = 0;

    private String customName;

    private NonNullList<ItemStack> inventory = NonNullList.withSize(4, ItemStack.EMPTY);

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        NBTTagList inventoryTagList = tag.getTagList("inventory", Constants.NBT.TAG_COMPOUND);

        for(int i = 0; i < inventoryTagList.tagCount(); i++){
            NBTTagCompound stackTag = inventoryTagList.getCompoundTagAt(i);
            int slot = stackTag.getShort("slot");
            this.inventory.set(slot, new ItemStack(stackTag));
        }

        this.cooldown = tag.getInteger("cooldown");
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        NBTTagList inventoryTagList = new NBTTagList();

        for(int i = 0; i < this.getSizeInventory(); i++){
            NBTTagCompound stackTag = new NBTTagCompound();
            stackTag.setShort("slot", (short)i);
            this.inventory.get(i).writeToNBT(stackTag);
            inventoryTagList.appendTag(stackTag);
        }

        tag.setTag("inventory", inventoryTagList);
        tag.setInteger("cooldown", this.cooldown);
        return tag;
    }

    @Override
    public int getField(int id)
    {
        switch (id)
        {
            case 0:
                return getCooldown();
            case 1:
                return getDefaultCooldown();
            case 2:
                return (int) getProgress();
            case 3:
                return 100;
            default:
                return 0;
        }
    }

    @Override
    public void setField(int id, int value)
    {
        switch (id)
        {
            case 0:
                this.cooldown = value;
                break;
            case 1:
                this.defaultCooldown = value;
                break;
            case 2:
                this.progress = value;
                break;
            case 3:
                break;
        }
    }

    @Override
    public int getFieldCount() {
        return 4;
    }

    @Override
    public void update() {
        if(!this.world.isRemote) {

            if(!this.isBurning()) {

                if(this.cooldown <= 0) {
                    this.cooldown = this.defaultCooldown;

                    Map<ItemStack, ItemStack> entries = RegistryParticleAccelerator.instance().getResult(this.getStackInSlot(1));

                    Map.Entry<ItemStack, ItemStack> entry = entries.entrySet().stream().findFirst().filter(Objects::nonNull).get();

                    if (this.inventory.get(2).isEmpty()) {
                        this.inventory.set(2, entry.getKey().copy());
                    }else{
                        this.inventory.get(2).grow(entry.getKey().getCount());
                    }

                    if (this.inventory.get(3).isEmpty()) {
                        this.inventory.set(3, entry.getValue().copy());
                    }else{
                        this.inventory.get(3).grow(entry.getValue().getCount());
                    }

                    this.inventory.get(0).shrink(1);
                    this.inventory.get(1).shrink(1);

                    if(this.inventory.get(0).isEmpty()) {
                        this.inventory.set(0, ItemStack.EMPTY);
                    }
                    if(this.inventory.get(1).isEmpty()) {
                        this.inventory.set(1, ItemStack.EMPTY);
                    }
                    if(this.inventory.get(2).isEmpty()) {
                        this.inventory.set(2, ItemStack.EMPTY);
                    }
                    if(this.inventory.get(3).isEmpty()) {
                        this.inventory.set(3, ItemStack.EMPTY);
                    }
                }
            }

            if(this.isBurning() && this.canUse()) {
                this.cooldown--;
                this.progress = ((double) (this.defaultCooldown - this.cooldown) / (double) this.defaultCooldown) * 100;
            }else if (!this.canUse()){
                this.cooldown = this.defaultCooldown;
            }

            this.markDirty();
        }
    }

    private boolean canUse(){
        if ((!this.inventory.get(0).isEmpty() && this.inventory.get(0).isItemEqual(new ItemStack(Items.REDSTONE)))&& !this.inventory.get(1).isEmpty() && ((this.inventory.get(2).isEmpty() && this.inventory.get(3).isEmpty()) || (this.inventory.get(2).isStackable() && this.inventory.get(3).isStackable() && this.inventory.get(2).getCount() < this.inventory.get(2).getMaxStackSize() && this.inventory.get(3).getCount() < this.inventory.get(3).getMaxStackSize()))){
            if (isRecipe(this.getStackInSlot(1))){
                return true;
            }
            return false;
        }
        return false;
    }

    public double getProgress(){
        return progress;
    }

    public int getCooldown() {
        return cooldown;
    }

    public int getDefaultCooldown() {
        return defaultCooldown;
    }

    //Inventory

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[4];
    }

    @Override
    public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction) {
        return true;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return true;
    }

    @Override
    public int getSizeInventory() {
        return this.inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack stack : this.inventory){
            return !stack.isEmpty();
        }

        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.inventory.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack stack = this.getStackInSlot(index);

        if(!stack.isEmpty()){
            if(stack.getCount() <= count){
                this.setInventorySlotContents(index, ItemStack.EMPTY);
                this.markDirty();
                return stack;
            }

            ItemStack splitStack = stack.splitStack(count);

            if(splitStack.getCount() == 0){
                this.setInventorySlotContents(index, ItemStack.EMPTY);
            }
            else{
                this.setInventorySlotContents(index, stack);
            }

            this.markDirty();
            return splitStack;
        }

        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack stack = this.getStackInSlot(index);
        this.setInventorySlotContents(index, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.inventory.set(index, stack);
        this.markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return player.getDistanceSq(this.pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    public void clear() {
        for(int i = 0; i < this.getSizeInventory(); i++){
            this.setInventorySlotContents(i, ItemStack.EMPTY);
        }

        this.markDirty();
    }

    public boolean isBurning() {
        return this.cooldown > 0;
    }

    private boolean isRecipe(ItemStack stack){
        ItemStack itemstack = ItemStack.EMPTY;
        Map<ItemStack, ItemStack> entries = RegistryParticleAccelerator.instance().getResult(stack);
        if (entries != null) {
            Map.Entry<ItemStack, ItemStack> entry = entries.entrySet().stream().filter(Objects::nonNull).findFirst().get();
            itemstack = entry.getKey();
        }

        if (itemstack.isEmpty())
        {
            return false;
        }
        else
        {
            ItemStack itemstack1 = (this.getStackInSlot(2));
            if (itemstack1.isEmpty()) return true;
            if (!itemstack1.isItemEqual(itemstack)) return false;
            int result = itemstack1.getCount() + itemstack.getCount();
            return result <= this.getInventoryStackLimit() && result <= itemstack1.getMaxStackSize();
        }
    }

    public String getCustomName(){
        return customName;
    }

    public void setCustomName(String customName){
        this.customName = customName;
    }

    @Override
    public String getName() {
        return customName;
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null && !this.customName.equals("");
    }

    @Nullable
    @Override
    public ITextComponent getDisplayName() {
        return this.hasCustomName() ? new TextComponentString(this.customName) : new TextComponentString(world.getBlockState(pos).getBlock().getLocalizedName());
    }

}
