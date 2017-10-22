package com.stormy.lightningadditions.block.resource;

import com.stormy.lightningadditions.init.ModSounds;
import com.stormy.lightninglib.lib.block.BlockBase;
import com.stormy.lightninglib.lib.utils.KeyChecker;
import com.stormy.lightninglib.lib.utils.TranslateUtils;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static com.stormy.lightninglib.lib.utils.StringHelper.BOLD;
import static com.stormy.lightninglib.lib.utils.StringHelper.ITALIC;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
public class BlockNetherWater extends BlockBase
{
    public BlockNetherWater() {
        super(Material.WATER);
        setSoundType(SoundType.STONE);
        setHardness(1.0F);
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        return layer == BlockRenderLayer.SOLID || layer == BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
                                    EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemstack = playerIn.getHeldItemMainhand();
        playerIn.playSound(ModSounds.water_place, 2.0f, 1.0f);
        if (itemstack.isEmpty()) {
            return true; }

        else
        { Item item = playerIn.getHeldItemMainhand().getItem();
            if (item == Items.BUCKET) {
                itemstack.shrink(1);
                if (itemstack.isEmpty()) {
                    playerIn.setHeldItem(hand, new ItemStack(Items.WATER_BUCKET));
                } else if (!playerIn.inventory.addItemStackToInventory(new ItemStack(Items.WATER_BUCKET))) {
                    playerIn.dropItem(new ItemStack(Items.WATER_BUCKET), false);
                }

                return true;
            } else if (item == Items.GLASS_BOTTLE) {

                itemstack.shrink(1);

                ItemStack itemstack1 = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM),
                        PotionTypes.WATER);

                if (itemstack.isEmpty()) {
                    playerIn.setHeldItem(hand, itemstack1);
                } else if (!playerIn.inventory.addItemStackToInventory(itemstack1)) {
                    playerIn.dropItem(itemstack1, false);
                } else if (playerIn instanceof EntityPlayerMP) {
                    ((EntityPlayerMP) playerIn).sendContainerToPlayer(playerIn.inventoryContainer);
                }

                return true;
            }
            return false;
        }

    }
    //Custom Tooltip
    @Override
    public void addInformation(ItemStack par1ItemStack, @Nullable World world, List par3List, ITooltipFlag par4) {
        if (KeyChecker.isHoldingShift()) { par3List.add(TextFormatting.GOLD + ITALIC + TranslateUtils.toLocal("tooltip.block.nether_water.line1")); }
        else { par3List.add(TranslateUtils.toLocal("tooltip.item.hold") + " " + TextFormatting.AQUA + TextFormatting.ITALIC + TranslateUtils.toLocal("tooltip.item.shift")); }
    }
}
