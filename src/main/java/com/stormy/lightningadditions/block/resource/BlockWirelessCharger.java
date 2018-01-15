package com.stormy.lightningadditions.block.resource;

import com.stormy.lightningadditions.creativetab.CreativeTabLA;
import com.stormy.lightningadditions.tile.resource.TileEntityWirelessCharger;
import com.stormy.lightninglib.lib.block.BlockBase;
import com.stormy.lightninglib.lib.utils.KeyChecker;
import com.stormy.lightninglib.lib.utils.TranslateUtils;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import static com.stormy.lightninglib.lib.utils.StringHelper.BOLD;
import static com.stormy.lightninglib.lib.utils.StringHelper.ITALIC;

public class BlockWirelessCharger extends BlockBase implements ITileEntityProvider
{
    public static final PropertyBool POWERED = PropertyBool.create("powered");

    public BlockWirelessCharger()
    {
        super(Material.ROCK);
        this.setHardness(2);
        this.setResistance(10);
        this.setSoundType(SoundType.STONE);
        this.setCreativeTab(CreativeTabLA.LA_TAB);
        this.setDefaultState(this.blockState.getBaseState().withProperty(POWERED, false));
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos changedPos)
    {
        boolean newPowered = worldIn.isBlockIndirectlyGettingPowered(pos) > 0;
        worldIn.setBlockState(pos, state.withProperty(POWERED, newPowered));
        if (worldIn.getTileEntity(pos) != null){
            if (worldIn.getTileEntity(pos) instanceof TileEntityWirelessCharger){
                TileEntityWirelessCharger wirelessCharger = (TileEntityWirelessCharger) worldIn.getTileEntity(pos);
                wirelessCharger.setDisabled(newPowered);
            }
        }
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        worldIn.setBlockState(pos, state.withProperty(POWERED, worldIn.isBlockIndirectlyGettingPowered(pos) > 0));
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(POWERED, false);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos, state.withProperty(POWERED, false), 2);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        boolean powered = meta == 1;

        return this.getDefaultState().withProperty(POWERED, powered);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(POWERED) ? 1 : 0;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, POWERED);
    }

    @Override
    public TileEntity createNewTileEntity(World arg0, int arg1)
    {
        return new TileEntityWirelessCharger();
    }

    //Custom Tooltip
    @Override
    public void addInformation(ItemStack par1ItemStack, @Nullable World world, List par3List, ITooltipFlag par4) {
        if (KeyChecker.isHoldingShift()) { par3List.add(TextFormatting.AQUA + ITALIC + TranslateUtils.toLocal("tooltip.block.wireless_charger.line1")); }
        if (KeyChecker.isHoldingShift()) { par3List.add(TextFormatting.LIGHT_PURPLE + BOLD + TranslateUtils.toLocal("tooltip.block.wireless_charger.line2")); }
        else { par3List.add(TranslateUtils.toLocal("tooltip.item.hold") + " " + TextFormatting.AQUA + TextFormatting.ITALIC + TranslateUtils.toLocal("tooltip.item.shift")); }
    }

}