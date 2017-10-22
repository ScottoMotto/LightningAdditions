package com.stormy.lightningadditions.block.resource;

import com.stormy.lightningadditions.creativetab.CreativeTabLA;
import com.stormy.lightningadditions.tile.resource.TileEntityWirelessCharger;
import com.stormy.lightninglib.lib.block.BlockBase;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockWirelessCharger extends BlockBase implements ITileEntityProvider
{
    public BlockWirelessCharger()
    {
        super(Material.ROCK);
        this.setHardness(2);
        this.setResistance(10);
        this.setSoundType(SoundType.STONE);
        this.setCreativeTab(CreativeTabLA.LA_TAB);
    }


    @Override
    public TileEntity createNewTileEntity(World arg0, int arg1)
    { return new TileEntityWirelessCharger(); }


}
