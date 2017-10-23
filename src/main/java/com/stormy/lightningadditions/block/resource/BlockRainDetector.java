package com.stormy.lightningadditions.block.resource;

import com.stormy.lightningadditions.tile.resource.TileEntityRainDetector;
import com.stormy.lightninglib.lib.utils.KeyChecker;
import com.stormy.lightninglib.lib.utils.TranslateUtils;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

import static com.stormy.lightninglib.lib.utils.StringHelper.BOLD;
import static com.stormy.lightninglib.lib.utils.StringHelper.ITALIC;
import static com.stormy.lightninglib.lib.utils.StringHelper.UNDERLINE;

public class BlockRainDetector extends BlockContainer
{
    public BlockRainDetector()
    {
        super(Material.WOOD);
        this.setDefaultState(this.blockState.getBaseState().withProperty(POWER, Integer.valueOf(0)));
        this.setHardness(0.2F);
        this.setSoundType(SoundType.WOOD); }

    public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
    protected static final AxisAlignedBB DAYLIGHT_DETECTOR_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D);

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    { return new TileEntityRainDetector(); }

    public void updatePower(World w, BlockPos pos)
    {

        IBlockState state = w.getBlockState(pos);

        if (w.isRaining() && w.getBiome(pos).canRain() && w.canBlockSeeSky(pos))
        {

            if (w.isThundering())
            {

                if (state.getValue(POWER).intValue() != 8)
                    w.setBlockState(pos, state.withProperty(POWER, 8), 3);
                return;
            }

            if (state.getValue(POWER).intValue() != 4)
            {

                w.setBlockState(pos, state.withProperty(POWER, 4), 3);
                return;
            }

        }
        else if (state.getValue(POWER).intValue() != 0)
            w.setBlockState(pos, state.withProperty(POWER, 0));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    { return DAYLIGHT_DETECTOR_AABB; }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    { return blockState.getValue(POWER).intValue(); }

    protected Class<? extends TileEntity> tile()
    { return TileEntityRainDetector.class; }

    @Override
    public boolean isFullCube(IBlockState state)
    { return false; }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    { return false; }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    { return EnumBlockRenderType.MODEL; }

    @Override
    public BlockRenderLayer getBlockLayer()
    { return BlockRenderLayer.CUTOUT_MIPPED; }

    @Override
    public boolean canProvidePower(IBlockState state)
    { return true; }

    @Override
    public IBlockState getStateFromMeta(int meta)
    { return this.getDefaultState().withProperty(POWER, Integer.valueOf(meta)); }

    @Override
    public int getMetaFromState(IBlockState state)
    { return state.getValue(POWER).intValue(); }

    @Override
    protected BlockStateContainer createBlockState()
    { return new BlockStateContainer(this, new IProperty[] { POWER }); }

    //Custom Tooltip
    @Override
    public void addInformation(ItemStack par1ItemStack, @Nullable World world, List par3List, ITooltipFlag par4) {
        if (KeyChecker.isHoldingShift()) { par3List.add(TextFormatting.DARK_AQUA + UNDERLINE + TranslateUtils.toLocal("tooltip.block.rain_detector.line1")); }
        else { par3List.add(TranslateUtils.toLocal("tooltip.item.hold") + " " + TextFormatting.AQUA + TextFormatting.ITALIC + TranslateUtils.toLocal("tooltip.item.shift")); }
    }
}
