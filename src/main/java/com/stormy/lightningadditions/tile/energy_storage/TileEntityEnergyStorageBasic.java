package com.stormy.lightningadditions.tile.energy_storage;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class TileEntityEnergyStorageBasic extends TileEntityEnergyStorageBase {

    public TileEntityEnergyStorageBasic() {
        super(NonNullList.withSize(1, ItemStack.EMPTY), 1000, 1000, 50000);
    }

    @Override
    public void update() {

        if (this.getEnergyStored() > 0) {
            pushEnergy(this.world, this.pos);
        }

        if (this.getEnergyStored() < this.getMaxRF() && this.getEnergyStored() >= 0) {
            pullEnergy(this.world, this.pos);
        }

        this.markDirty();

    }

}
