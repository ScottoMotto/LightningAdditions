package com.stormy.lightninglib.lib.item;

import cofh.energy.IEnergyContainerItem;
import cofh.item.IMultiModeItem;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.stormy.lightninglib.lib.utils.EnergyHelper;
import com.stormy.lightninglib.lib.utils.ItemHelper;
import com.stormy.lightninglib.lib.utils.StringHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

import static com.stormy.lightninglib.lib.utils.StringHelper.BOLD;

public class ItemToolRF extends ItemToolBase implements IMultiModeItem, IEnergyContainerItem
{
    /**
     * Class: ItemToolRF
     * @author: TeamCoFH
     * Modified
     */
    protected int maxEnergy = 320000;
    protected int maxTransfer = 4000;
    protected int energyPerUse = 200;
    protected int energyPerUseCharged = 800;
    protected int damage = 0;

    public ItemToolRF(float baseDamage, float attackSpeed, ToolMaterial toolMaterial)
    {

        super(baseDamage, attackSpeed, toolMaterial);
        setNoRepair();
        addPropertyOverride(new ResourceLocation("static"), (stack, world, entity) -> ItemToolRF.this.getEnergyStored(stack) > 0 && !ItemToolRF.this.isEnhanced(stack) ? 1F : 0F);
        addPropertyOverride(new ResourceLocation("enhanced"), (stack, world, entity) -> ItemToolRF.this.isEnhanced(stack) ? 1F : 0F);
    }

    public ItemToolRF(float attackSpeed, ToolMaterial toolMaterial)
    { this(0, attackSpeed, toolMaterial); }


    public ItemToolRF setEnergyParams(int maxEnergy, int maxTransfer, int energyPerUse, int energyPerUseCharged)
    {
        this.maxEnergy = maxEnergy;
        this.maxTransfer = maxTransfer;
        this.energyPerUse = energyPerUse;
        this.energyPerUseCharged = energyPerUseCharged;
        return this;
    }

    protected boolean isEnhanced(ItemStack stack)
    { return getMode(stack) == 1 && getEnergyStored(stack) > energyPerUseCharged; }

    protected int getEnergyPerUse(ItemStack stack) {

        int unbreakingLevel = MathHelper.clamp(EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack), 0, 4);
        return (isEnhanced(stack) ? energyPerUseCharged : energyPerUse) * (5 - unbreakingLevel) / 5;
    }

    protected int useEnergy(ItemStack stack, boolean simulate) {

        int unbreakingLevel = MathHelper.clamp(EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack), 0, 4);
        return extractEnergy(stack, isEnhanced(stack) ? energyPerUseCharged * (5 - unbreakingLevel) / 5 : energyPerUse * (5 - unbreakingLevel) / 5, simulate);
    }

    @Override
    protected float getEfficiency(ItemStack stack)
    {
        if (isEnhanced(stack) && getEnergyStored(stack) >= energyPerUseCharged)
        { return efficiency * 1.5F; }
        return efficiency;
    }

    @Override
    protected boolean setShowInCreative() {
        return true;
    }

    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean advanced) {

        if (StringHelper.displayShiftForDetail && !StringHelper.isShiftKeyDown()) {
            list.add(StringHelper.shiftForDetails());
        }
        if (!StringHelper.isShiftKeyDown()) {
            return;
        }
        if (stack.getTagCompound() == null) {
            EnergyHelper.setDefaultEnergyTag(stack, 0);
        }
        list.add(StringHelper.localize("info.LA.charge") + ": " + StringHelper.formatNumber(stack.getTagCompound().getInteger("Energy")) + " / " + StringHelper.formatNumber(maxEnergy) + " RF");

        list.add(StringHelper.ORANGE + getEnergyPerUse(stack) + " " + StringHelper.localize("info.LA.tool.energyPerUse") + StringHelper.END);
        LAProps.addEnhancedTip(this, stack, list);
        if (getEnergyStored(stack) >= getEnergyPerUse(stack)) {
            int adjustedDamage = (int) (damage + player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue());
            list.add("");
            list.add(StringHelper.LIGHT_RED + BOLD + "+" + adjustedDamage + " " + StringHelper.localize("info.LA.damageAttack") + StringHelper.END);
            list.add(StringHelper.YELLOW + BOLD + "+" + (isEnhanced(stack) ? 2 : 1) + " " + StringHelper.localize("info.LA.damageEnhanced") + StringHelper.END);
        }
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(@Nonnull Item item, CreativeTabs tab, NonNullList<ItemStack> list) {

        if (showInCreative)
        {
            list.add(EnergyHelper.setDefaultEnergyTag(new ItemStack(item), 0));
            list.add(EnergyHelper.setDefaultEnergyTag(new ItemStack(item), maxEnergy)); }
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isCurrentItem) {

        if (stack.getItemDamage() > 0) {
            stack.setItemDamage(0);
        }
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {

        super.setDamage(stack, 0);
    }

    @Override
    public boolean getIsRepairable(ItemStack itemToRepair, ItemStack stack) {

        return false;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase entity, EntityLivingBase player) {

        if (stack.getItemDamage() > 0) {
            stack.setItemDamage(0);
        }
        EntityPlayer thePlayer = (EntityPlayer) player;

        if (thePlayer.capabilities.isCreativeMode || extractEnergy(stack, energyPerUse, false) == energyPerUse) {
            int fluxDamage = isEnhanced(stack) ? 2 : 1;

            float potionDamage = 1.0f;
            if (player.isPotionActive(MobEffects.STRENGTH)) {
                potionDamage += player.getActivePotionEffect(MobEffects.STRENGTH).getAmplifier() * 1.3f;
            }
        }
        return true;
    }

    @Override
    public boolean isDamaged(ItemStack stack)
    { return true; }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    { return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged) && (slotChanged || !ItemHelper.areItemStacksEqualIgnoreTags(oldStack, newStack, "Energy")); }

    @Override
    public boolean showDurabilityBar(ItemStack stack)
    { return LAProps.showToolCharge && stack.getTagCompound() != null && !stack.getTagCompound().getBoolean("CreativeTab"); }

    @Override
    public int getMaxDamage(ItemStack stack)
    { return 0; }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {

        if (getEnergyStored(stack) < energyPerUse) {
            return 1.0F;
        }
        return super.getDestroySpeed(stack, state);
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {

        if (stack.getTagCompound() == null) {
            EnergyHelper.setDefaultEnergyTag(stack, 0);
        }
        return 1D - (double) stack.getTagCompound().getInteger("Energy") / (double) maxEnergy;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {

        return isEnhanced(stack) ? EnumRarity.RARE : EnumRarity.UNCOMMON;
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {

        Multimap<String, AttributeModifier> multimap = HashMultimap.create();

        if (slot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", (double) this.attackSpeed, 0));

            if (extractEnergy(stack, energyPerUse, true) == energyPerUse) {
                int fluxDamage = isEnhanced(stack) ? 2 : 1;
                multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", fluxDamage + damage, 0));
            } else {
                multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", 1, 0));
            }
        }
        return multimap;
    }

    /* IMultiModeItem */
    @Override
    public int getMode(ItemStack stack) {

        return !stack.hasTagCompound() ? 0 : stack.getTagCompound().getInteger("Mode");
    }

    @Override
    public boolean setMode(ItemStack stack, int mode) {

        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        stack.getTagCompound().setInteger("Mode", mode);
        return false;
    }

    @Override
    public boolean incrMode(ItemStack stack) {

        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        int curMode = getMode(stack);
        curMode++;
        if (curMode >= getNumModes(stack)) {
            curMode = 0;
        }
        stack.getTagCompound().setInteger("Mode", curMode);
        return true;
    }

    @Override
    public boolean decrMode(ItemStack stack) {

        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        int curMode = getMode(stack);
        curMode--;
        if (curMode <= 0) {
            curMode = getNumModes(stack) - 1;
        }
        stack.getTagCompound().setInteger("Mode", curMode);
        return true;
    }

    @Override
    public int getNumModes(ItemStack stack) {

        return 2;
    }

    @Override
    public void onModeChange(EntityPlayer player, ItemStack stack) {

        if (isEnhanced(stack)) {
            player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.PLAYERS, 0.4F, 1.0F);
        } else {
            player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.2F, 0.6F);
        }
    }

    /* IEnergyContainerItem */
    @Override
    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {

        if (container.getTagCompound() == null) {
            EnergyHelper.setDefaultEnergyTag(container, 0);
        }
        int stored = container.getTagCompound().getInteger("Energy");
        int receive = Math.min(maxReceive, Math.min(maxEnergy - stored, maxTransfer));

        if (!simulate) {
            stored += receive;
            container.getTagCompound().setInteger("Energy", stored);
        }
        return receive;
    }

    @Override
    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {

        if (container.getTagCompound() == null) {
            EnergyHelper.setDefaultEnergyTag(container, 0);
        }
        if (container.getTagCompound().hasKey("Unbreakable")) {
            container.getTagCompound().removeTag("Unbreakable");
        }
        int stored = container.getTagCompound().getInteger("Energy");
        int extract = Math.min(maxExtract, stored);

        if (!simulate) {
            stored -= extract;
            container.getTagCompound().setInteger("Energy", stored);

            if (stored == 0) {
                setMode(container, 0);
            }
        }
        return extract;
    }

    @Override
    public int getEnergyStored(ItemStack container) {

        if (container.getTagCompound() == null) {
            EnergyHelper.setDefaultEnergyTag(container, 0);
        }
        return container.getTagCompound().getInteger("Energy");
    }

    @Override
    public int getMaxEnergyStored(ItemStack container) {

        return maxEnergy;
    }

    /* CAPABILITIES */
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {

        return new EnergyContainerItemWrapper(stack, this);
    }

    public static class EnergyContainerItemWrapper implements ICapabilityProvider
    {
        final ItemStack stack;
        final IEnergyContainerItem container;
        final boolean canExtract;
        final boolean canReceive;
        final net.minecraftforge.energy.IEnergyStorage energyCap;

        public EnergyContainerItemWrapper(ItemStack stackIn, IEnergyContainerItem containerIn, boolean extractIn, boolean receiveIn) {

            this.stack = stackIn;
            this.container = containerIn;

            this.canExtract = extractIn;
            this.canReceive = receiveIn;

            this.energyCap = new net.minecraftforge.energy.IEnergyStorage() {

                @Override
                public int receiveEnergy(int maxReceive, boolean simulate) {

                    return container.receiveEnergy(stack, maxReceive, simulate);
                }

                @Override
                public int extractEnergy(int maxExtract, boolean simulate) {

                    return container.extractEnergy(stack, maxExtract, simulate);
                }

                @Override
                public int getEnergyStored() {

                    return container.getEnergyStored(stack);
                }

                @Override
                public int getMaxEnergyStored() {

                    return container.getMaxEnergyStored(stack);
                }

                @Override
                public boolean canExtract() {

                    return canExtract;
                }

                @Override
                public boolean canReceive() {

                    return canReceive;
                }
            };
        }

        public EnergyContainerItemWrapper(ItemStack stackIn, IEnergyContainerItem containerIn) {

            this(stackIn, containerIn, true, true);
        }

        /* ICapabilityProvider */
        @Override
        public boolean hasCapability(Capability<?> capability, EnumFacing from) {

            return capability == CapabilityEnergy.ENERGY;
        }

        @Override
        public <T> T getCapability(Capability<T> capability, final EnumFacing from) {

            if (!hasCapability(capability, from)) {
                return null;
            }
            return CapabilityEnergy.ENERGY.cast(energyCap);
        }


    }

}