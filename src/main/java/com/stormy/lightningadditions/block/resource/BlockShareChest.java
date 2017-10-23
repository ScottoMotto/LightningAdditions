package com.stormy.lightningadditions.block.resource;

import com.stormy.lightningadditions.LightningAdditions;
import com.stormy.lightningadditions.network.GuiHandler;
import com.stormy.lightningadditions.tile.resource.TileEntitySharedChest;
import com.stormy.lightninglib.lib.utils.KeyChecker;
import com.stormy.lightninglib.lib.utils.TranslateUtils;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import static com.stormy.lightninglib.lib.utils.StringHelper.ITALIC;

public class BlockShareChest extends BlockContainer
{
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    protected static final AxisAlignedBB ENDER_CHEST_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);

    public BlockShareChest()
    {
        super(Material.ROCK);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.setHardness(0.7F); }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    { return ENDER_CHEST_AABB; }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    { return false; }

    @Override
    public boolean isFullCube(IBlockState state)
    { return false; }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasCustomBreakingProgress(IBlockState state)
    { return true; }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    { return EnumBlockRenderType.ENTITYBLOCK_ANIMATED; }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    { return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()); }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    { ((TileEntitySharedChest) worldIn.getTileEntity(pos)).uuid = placer.getUniqueID(); }

    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    { return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()); }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    { if (!worldIn.isRemote)
            playerIn.openGui(LightningAdditions.INSTANCE, GuiHandler.gui_id_shared_chest, worldIn, pos.getX(), pos.getY(), pos.getZ());
        else ((TileEntitySharedChest) worldIn.getTileEntity(pos)).openChest();
        return true; }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    { return new TileEntitySharedChest(); }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        for (int i = 0; i < 3; ++i)
        {
            int j = rand.nextInt(2) * 2 - 1;
            int k = rand.nextInt(2) * 2 - 1;
            double d0 = pos.getX() + 0.5D + 0.25D * j;
            double d1 = pos.getY() + rand.nextFloat();
            double d2 = pos.getZ() + 0.5D + 0.25D * k;
            double d3 = rand.nextFloat() * j;
            double d4 = (rand.nextFloat() - 0.5D) * 0.125D;
            double d5 = rand.nextFloat() * k;
            worldIn.spawnParticle(EnumParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getFront(meta);
        if (enumfacing.getAxis() == EnumFacing.Axis.Y)
        { enumfacing = EnumFacing.NORTH; }
        return this.getDefaultState().withProperty(FACING, enumfacing); }

    @Override
    public int getMetaFromState(IBlockState state)
    { return state.getValue(FACING).getIndex(); }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot)
    { return state.withProperty(FACING, rot.rotate(state.getValue(FACING))); }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    { return state.withRotation(mirrorIn.toRotation(state.getValue(FACING))); }

    @Override
    protected BlockStateContainer createBlockState()
    { return new BlockStateContainer(this, new IProperty[] { FACING }); }

    public BlockFaceShape getBlockFaceShape(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_)
    { return BlockFaceShape.UNDEFINED; }

    protected Class<? extends TileEntity> tile()
    { return TileEntitySharedChest.class; }

    //Custom Tooltip
    @Override
    public void addInformation(ItemStack par1ItemStack, @Nullable World world, List par3List, ITooltipFlag par4) {
        if (KeyChecker.isHoldingShift()) { par3List.add(TextFormatting.LIGHT_PURPLE + ITALIC + TranslateUtils.toLocal("tooltip.block.shared_chest.line1")); }
        else { par3List.add(TranslateUtils.toLocal("tooltip.item.hold") + " " + TextFormatting.AQUA + TextFormatting.ITALIC + TranslateUtils.toLocal("tooltip.item.shift")); }
    }
}
