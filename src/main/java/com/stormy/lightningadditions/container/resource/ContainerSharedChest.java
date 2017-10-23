package com.stormy.lightningadditions.container.resource;

import com.stormy.lightningadditions.block.base.BlockContainerLA;
import com.stormy.lightningadditions.tile.resource.TileEntitySharedChest;
import com.stormy.lightninglib.lib.utils.OfflinePlayerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ContainerSharedChest extends Container
{
    private final TileEntitySharedChest lowerChestInventory;
    private final int numRows;
    private InventoryEnderChest te;

    public ContainerSharedChest(IInventory playerInventory, TileEntitySharedChest chestInventory, EntityPlayer playerOpen)
    {
        this.lowerChestInventory = chestInventory;
        this.numRows = 3;
        chestInventory.openInventory(playerOpen);
        chestInventory.openChest();

        EntityPlayer player;

        player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(lowerChestInventory.uuid);

        te = player == null ? OfflinePlayerUtils.getOfflineEnderChest(chestInventory.uuid) : player.getInventoryEnderChest();

        int i = (this.numRows - 4) * 18;

        for (int j = 0; j < this.numRows; ++j)
        {
            for (int k = 0; k < 9; ++k)
            {
                this.addSlotToContainer(new Slot(te, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }

        for (int l = 0; l < 3; ++l)
        {
            for (int j1 = 0; j1 < 9; ++j1)
            {
                this.addSlotToContainer(new Slot(playerInventory, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + i));
            }
        }

        for (int i1 = 0; i1 < 9; ++i1)
        {
            this.addSlotToContainer(new Slot(playerInventory, i1, 8 + i1 * 18, 161 + i));
        }
    }

    public ContainerSharedChest(InventoryPlayer inventory, TileEntitySharedChest tileEntity) {
        numRows = 0;
        lowerChestInventory = null;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn)
    { return true; }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < this.numRows * 9)
            {
                if (!this.mergeItemStack(itemstack1, this.numRows * 9, this.inventorySlots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, this.numRows * 9, false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);
        this.lowerChestInventory.closeInventory(playerIn);
        this.lowerChestInventory.closeChest(); }
}
