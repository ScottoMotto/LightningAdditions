package com.stormy.lightninglib.lib.utils;

import cofh.item.IInventoryContainerItem;
import cofh.item.IMultiModeItem;
import com.google.common.base.Strings;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static net.minecraftforge.oredict.OreDictionary.WILDCARD_VALUE;

/**
 * Contains various helper functions to assist with {@link Item} and {@link ItemStack} manipulation and interaction.
 *
 * @author King Lemming // TeamCoFH
 * Modified
 */
public final class ItemHelper {

    public static final String BLOCK = "block";
    public static final String ORE = "ore";
    public static final String DUST = "dust";
    public static final String INGOT = "ingot";
    public static final String NUGGET = "nugget";
    public static final String LOG = "log";

    private ItemHelper() {

    }

    public static boolean isPlayerHoldingSomething(EntityPlayer player) {

        return !player.getHeldItemMainhand().isEmpty() || !player.getHeldItemOffhand().isEmpty();
    }

    public static ItemStack getMainhandStack(EntityPlayer player) {

        return player.getHeldItemMainhand();
    }

    public static ItemStack getOffhandStack(EntityPlayer player) {

        return player.getHeldItemOffhand();
    }

    public static ItemStack getHeldStack(EntityPlayer player) {

        ItemStack stack = player.getHeldItemMainhand();
        if (stack.isEmpty()) {
            stack = player.getHeldItemOffhand();
        }
        return stack;
    }

    public static ItemStack cloneStack(Item item, int stackSize) {

        if (item == null) {
            return ItemStack.EMPTY;
        }
        return new ItemStack(item, stackSize);
    }

    public static ItemStack cloneStack(Block block, int stackSize) {

        if (block == null) {
            return ItemStack.EMPTY;
        }
        return new ItemStack(block, stackSize);
    }

    public static ItemStack cloneStack(ItemStack stack, int stackSize) {

        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack retStack = stack.copy();
        retStack.setCount(stackSize);

        return retStack;
    }

    public static ItemStack cloneStack(ItemStack stack) {

        return stack.isEmpty() ? ItemStack.EMPTY : stack.copy();
    }

    public static ItemStack copyTag(ItemStack container, ItemStack other) {

        if (!other.isEmpty() && other.hasTagCompound()) {
            container.setTagCompound(other.getTagCompound().copy());
        }
        return container;
    }

    public static NBTTagCompound setItemStackTagName(NBTTagCompound tag, String name) {

        if (Strings.isNullOrEmpty(name)) {
            return null;
        }
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        if (!tag.hasKey("display")) {
            tag.setTag("display", new NBTTagCompound());
        }
        tag.getCompoundTag("display").setString("Name", name);

        return tag;
    }

    public static ItemStack readItemStackFromNBT(NBTTagCompound nbt) {

        ItemStack stack = new ItemStack(Item.getItemById(nbt.getShort("id")));
        stack.setCount(nbt.getInteger("Count"));
        stack.setItemDamage(Math.max(0, nbt.getShort("Damage")));

        if (nbt.hasKey("tag", 10)) {
            stack.setTagCompound(nbt.getCompoundTag("tag"));
        }
        return stack;
    }

    public static NBTTagCompound writeItemStackToNBT(ItemStack stack, NBTTagCompound nbt) {

        nbt.setShort("id", (short) Item.getIdFromItem(stack.getItem()));
        nbt.setInteger("Count", stack.getCount());
        nbt.setShort("Damage", (short) getItemDamage(stack));

        if (stack.hasTagCompound()) {
            nbt.setTag("tag", stack.getTagCompound());
        }
        return nbt;
    }

    public static NBTTagCompound writeItemStackToNBT(ItemStack stack, int amount, NBTTagCompound nbt) {

        nbt.setShort("id", (short) Item.getIdFromItem(stack.getItem()));
        nbt.setInteger("Count", amount);
        nbt.setShort("Damage", (short) getItemDamage(stack));

        if (stack.hasTagCompound()) {
            nbt.setTag("tag", stack.getTagCompound());
        }
        return nbt;
    }

    public static String getNameFromItemStack(ItemStack stack) {

        if (stack.isEmpty() || !stack.hasTagCompound() || !stack.getTagCompound().hasKey("display")) {
            return "";
        }
        return stack.getTagCompound().getCompoundTag("display").getString("Name");
    }

    public static ItemStack damageItem(ItemStack stack, int amt, Random rand) {

        if (!stack.isEmpty() && stack.isItemStackDamageable()) {
            stack.shrink(1);
            if (stack.getCount() <= 0) {
                stack = ItemStack.EMPTY;
            } else {
                stack.setItemDamage(0);
            }
        }
        return stack;
    }

    public static ItemStack consumeItem(ItemStack stack) {

        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        Item item = stack.getItem();
        boolean largerStack = stack.getCount() > 1;
        // vanilla only alters the stack passed to hasContainerItem/etc. when the size is >1

        if (largerStack) {
            stack.shrink(1);
        }
        if (item.hasContainerItem(stack)) {
            ItemStack ret = item.getContainerItem(stack);

            if (ret.isEmpty()) {
                return ItemStack.EMPTY;
            }
            if (ret.isItemStackDamageable() && ret.getItemDamage() > ret.getMaxDamage()) {
                ret = ItemStack.EMPTY;
            }
            return ret;
        }
        return largerStack ? stack : ItemStack.EMPTY;
    }

    public static ItemStack consumeItem(ItemStack stack, EntityPlayer player) {

        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        Item item = stack.getItem();
        boolean largerStack = stack.getCount() > 1;
        // vanilla only alters the stack passed to hasContainerItem/etc. when the size is >1

        if (largerStack) {
            stack.shrink(1);
        }
        if (item.hasContainerItem(stack)) {
            ItemStack ret = item.getContainerItem(stack);

            if (ret.isEmpty() || (ret.isItemStackDamageable() && ret.getItemDamage() > ret.getMaxDamage())) {
                ret = ItemStack.EMPTY;
            }
            if (stack.getCount() < 1) {
                return ret;
            }
            if (!ret.isEmpty() && !player.inventory.addItemStackToInventory(ret)) {
                player.dropItem(ret, false, true);
            }
        }
        return largerStack ? stack : ItemStack.EMPTY;
    }

    public static boolean disposePlayerItem(ItemStack stack, ItemStack dropStack, EntityPlayer entityplayer, boolean allowDrop) {

        return disposePlayerItem(stack, dropStack, entityplayer, allowDrop, true);
    }

    public static boolean disposePlayerItem(ItemStack stack, ItemStack dropStack, EntityPlayer entityplayer, boolean allowDrop, boolean allowReplace) {

        if (entityplayer == null || entityplayer.capabilities.isCreativeMode) {
            return true;
        }
        if (allowReplace && stack.getCount() <= 1) {
            entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, ItemStack.EMPTY);
            entityplayer.inventory.addItemStackToInventory(dropStack);
            return true;
        } else if (allowDrop) {
            stack.shrink(1);
            if (!dropStack.isEmpty() && !entityplayer.inventory.addItemStackToInventory(dropStack)) {
                entityplayer.dropItem(dropStack, false, true);
            }
            return true;
        }
        return false;
    }

    /**
     * This prevents an overridden getDamage() call from messing up metadata acquisition.
     */
    public static int getItemDamage(ItemStack stack) {

        return Items.DIAMOND.getDamage(stack);
    }

    /* CREATING ItemStacks */
    public static ItemStack stack(Item t) {

        return new ItemStack(t);
    }

    public static ItemStack stack(Item t, int s) {

        return new ItemStack(t, s);
    }

    public static ItemStack stack(Item t, int s, int m) {

        return new ItemStack(t, s, m);
    }

    public static ItemStack stack(Block t) {

        return new ItemStack(t);
    }

    public static ItemStack stack(Block t, int s) {

        return new ItemStack(t, s);
    }

    public static ItemStack stack(Block t, int s, int m) {

        return new ItemStack(t, s, m);
    }

    public static ItemStack stack2(Item t) {

        return new ItemStack(t, 1, WILDCARD_VALUE);
    }

    public static ItemStack stack2(Item t, int s) {

        return new ItemStack(t, s, WILDCARD_VALUE);
    }

    public static ItemStack stack2(Block t) {

        return new ItemStack(t, 1, WILDCARD_VALUE);
    }

    public static ItemStack stack2(Block t, int s) {

        return new ItemStack(t, s, WILDCARD_VALUE);
    }

    // }

    // SMELTING{
    public static boolean addSmelting(ItemStack out, Item in) {

        if (out.isEmpty() | in == null) {
            return false;
        }
        FurnaceRecipes.instance().addSmeltingRecipe(cloneStack(in, 1), cloneStack(out), 0);
        return true;
    }

    public static boolean addSmelting(ItemStack out, Block in) {

        if (out.isEmpty() | in == null) {
            return false;
        }
        FurnaceRecipes.instance().addSmeltingRecipe(cloneStack(in, 1), cloneStack(out), 0);
        return true;
    }

    public static boolean addSmelting(ItemStack out, ItemStack in) {

        if (out.isEmpty() | in.isEmpty()) {
            return false;
        }
        FurnaceRecipes.instance().addSmeltingRecipe(cloneStack(in, 1), cloneStack(out), 0);
        return true;
    }

    public static boolean addSmelting(ItemStack out, Item in, float XP) {

        if (out.isEmpty() | in == null) {
            return false;
        }
        FurnaceRecipes.instance().addSmeltingRecipe(cloneStack(in, 1), cloneStack(out), XP);
        return true;
    }

    public static boolean addSmelting(ItemStack out, Block in, float XP) {

        if (out.isEmpty() | in == null) {
            return false;
        }
        FurnaceRecipes.instance().addSmeltingRecipe(cloneStack(in, 1), cloneStack(out), XP);
        return true;
    }

    public static boolean addSmelting(ItemStack out, ItemStack in, float XP) {

        if (out.isEmpty() | in.isEmpty()) {
            return false;
        }
        FurnaceRecipes.instance().addSmeltingRecipe(cloneStack(in, 1), cloneStack(out), XP);
        return true;
    }

    public static boolean addWeakSmelting(ItemStack out, Item in) {

        if (out.isEmpty() | in == null) {
            return false;
        }
        FurnaceRecipes.instance().addSmeltingRecipe(cloneStack(in, 1), cloneStack(out), 0.1f);
        return true;
    }

    public static boolean addWeakSmelting(ItemStack out, Block in) {

        if (out.isEmpty() | in == null) {
            return false;
        }
        FurnaceRecipes.instance().addSmeltingRecipe(cloneStack(in, 1), cloneStack(out), 0.1f);
        return true;
    }

    public static boolean addWeakSmelting(ItemStack out, ItemStack in) {

        if (out.isEmpty() | in.isEmpty()) {
            return false;
        }
        FurnaceRecipes.instance().addSmeltingRecipe(cloneStack(in, 1), cloneStack(out), 0.1f);
        return true;
    }

    public static void registerWithHandlers(String oreName, ItemStack stack) {

        OreDictionary.registerOre(oreName, stack);
        //GameRegistry.registerCustomItemStack(oreName, stack);
        FMLInterModComms.sendMessage("ForgeMicroblock", "microMaterial", stack);
    }

    /* MULTIMODE ITEM HELPERS */
    public static boolean isPlayerHoldingMultiModeItem(EntityPlayer player) {

        if (!isPlayerHoldingSomething(player)) {
            return false;
        }
        ItemStack heldItem = getHeldStack(player);
        Item equipped = heldItem.getItem();
        return equipped instanceof IMultiModeItem;
    }

    public static boolean incrHeldMultiModeItemState(EntityPlayer player) {

        if (!isPlayerHoldingSomething(player)) {
            return false;
        }
        ItemStack heldItem = getHeldStack(player);
        Item equipped = heldItem.getItem();
        IMultiModeItem multiModeItem = (IMultiModeItem) equipped;

        return multiModeItem.incrMode(heldItem);
    }

    public static boolean decrHeldMultiModeItemState(EntityPlayer player) {

        if (!isPlayerHoldingSomething(player)) {
            return false;
        }
        ItemStack equipped = getHeldStack(player);
        IMultiModeItem multiModeItem = (IMultiModeItem) equipped.getItem();

        return multiModeItem.incrMode(equipped);
    }

    public static boolean setHeldMultiModeItemState(EntityPlayer player, int mode) {

        if (!isPlayerHoldingSomething(player)) {
            return false;
        }
        ItemStack equipped = getHeldStack(player);
        IMultiModeItem multiModeItem = (IMultiModeItem) equipped.getItem();

        return multiModeItem.setMode(equipped, mode);
    }

    public static boolean isPlayerHoldingEnergyContainerItem(EntityPlayer player) {

        return EnergyHelper.isPlayerHoldingEnergyContainerItem(player);
    }

    public static boolean isPlayerHoldingNothing(EntityPlayer player) {

        return getHeldStack(player).isEmpty();
    }

    public static Item getItemFromStack(ItemStack theStack) {

        return theStack.isEmpty() ? null : theStack.getItem();
    }

    public static boolean areItemsEqual(Item itemA, Item itemB) {

        if (itemA == null | itemB == null) {
            return false;
        }
        return itemA == itemB || itemA.equals(itemB);
    }

    public static boolean isPlayerHoldingItem(Class<?> item, EntityPlayer player) {

        return item.isInstance(getItemFromStack(getHeldStack(player)));
    }

    /**
     * Determine if a player is holding an ItemStack of a specific Item type.
     */
    public static boolean isPlayerHoldingItem(Item item, EntityPlayer player) {

        return areItemsEqual(item, getItemFromStack(getHeldStack(player)));
    }

    public static boolean isPlayerHoldingMainhand(Item item, EntityPlayer player) {

        return areItemsEqual(item, getItemFromStack(getMainhandStack(player)));
    }

    public static boolean isPlayerHoldingOffhand(Item item, EntityPlayer player) {

        return areItemsEqual(item, getItemFromStack(getOffhandStack(player)));
    }

    /**
     * Determine if a player is holding an ItemStack with a specific Item ID and Metadata.
     */
    public static boolean isPlayerHoldingItemStack(ItemStack stack, EntityPlayer player) {

        return itemsEqualWithMetadata(stack, getHeldStack(player));
    }

    /**
     * Determine if the damage of two ItemStacks is equal. Assumes both itemstacks are of type A.
     */
    public static boolean itemsDamageEqual(ItemStack stackA, ItemStack stackB) {

        return (!stackA.getHasSubtypes() && stackA.getMaxDamage() == 0) || (getItemDamage(stackA) == getItemDamage(stackB));
    }

    /**
     * Determine if two ItemStacks have the same Item.
     */
    public static boolean itemsEqualWithoutMetadata(ItemStack stackA, ItemStack stackB) {

        if (stackA.isEmpty() || stackB.isEmpty()) {
            return false;
        }
        return areItemsEqual(stackA.getItem(), stackB.getItem());
    }

    /**
     * Determine if two ItemStacks have the same Item and NBT.
     */
    public static boolean itemsEqualWithoutMetadata(ItemStack stackA, ItemStack stackB, boolean checkNBT) {

        return itemsEqualWithoutMetadata(stackA, stackB) && (!checkNBT || doNBTsMatch(stackA.getTagCompound(), stackB.getTagCompound()));
    }

    /**
     * Determine if two ItemStacks have the same Item and damage.
     */
    public static boolean itemsEqualWithMetadata(ItemStack stackA, ItemStack stackB) {

        return itemsEqualWithoutMetadata(stackA, stackB) && itemsDamageEqual(stackA, stackB);
    }

    /**
     * Determine if two ItemStacks have the same Item, damage, and NBT.
     */
    public static boolean itemsEqualWithMetadata(ItemStack stackA, ItemStack stackB, boolean checkNBT) {

        return itemsEqualWithMetadata(stackA, stackB) && (!checkNBT || doNBTsMatch(stackA.getTagCompound(), stackB.getTagCompound()));
    }

    /**
     * Determine if two ItemStacks have the same Item, identical damage, and NBT.
     */
    public static boolean itemsIdentical(ItemStack stackA, ItemStack stackB) {

        return itemsEqualWithoutMetadata(stackA, stackB) && getItemDamage(stackA) == getItemDamage(stackB) && doNBTsMatch(stackA.getTagCompound(), stackB.getTagCompound());
    }

    /**
     * Determine if two NBTTagCompounds are equal.
     */
    public static boolean doNBTsMatch(NBTTagCompound nbtA, NBTTagCompound nbtB) {

        if (nbtA == null & nbtB == null) {
            return true;
        }
        if (nbtA != null & nbtB != null) {
            return nbtA.equals(nbtB);
        }
        return false;
    }

    public static boolean itemsEqualForCrafting(ItemStack stackA, ItemStack stackB) {

        return itemsEqualWithoutMetadata(stackA, stackB) && (!stackA.getHasSubtypes() || ((getItemDamage(stackA) == OreDictionary.WILDCARD_VALUE || getItemDamage(stackB) == OreDictionary.WILDCARD_VALUE) || getItemDamage(stackB) == getItemDamage(stackA)));
    }

    public static boolean isBlacklist(ItemStack output) {

        Item item = output.getItem();
        return Item.getItemFromBlock(Blocks.BIRCH_STAIRS) == item || Item.getItemFromBlock(Blocks.JUNGLE_STAIRS) == item || Item.getItemFromBlock(Blocks.OAK_STAIRS) == item || Item.getItemFromBlock(Blocks.SPRUCE_STAIRS) == item || Item.getItemFromBlock(Blocks.PLANKS) == item || Item.getItemFromBlock(Blocks.WOODEN_SLAB) == item;
    }

    public static String getItemNBTString(ItemStack theItem, String nbtKey, String invalidReturn) {

        return theItem.getTagCompound() != null && theItem.getTagCompound().hasKey(nbtKey) ? theItem.getTagCompound().getString(nbtKey) : invalidReturn;
    }

    /**
     * Adds Inventory information to ItemStacks which themselves hold things. Called in addInformation().
     */
    public static void addInventoryInformation(ItemStack stack, List<String> list) {

        addInventoryInformation(stack, list, 0, Integer.MAX_VALUE);
    }

    public static void addInventoryInformation(ItemStack stack, List<String> list, int minSlot, int maxSlot) {

        if (stack.getTagCompound() == null) {
            list.add(StringHelper.localize("info.LA.empty"));
            return;
        }
        if (stack.getItem() instanceof IInventoryContainerItem && stack.getTagCompound().hasKey("Accessible")) {
            addAccessibleInventoryInformation(stack, list, minSlot, maxSlot);
            return;
        }
        if (!stack.getTagCompound().hasKey("Inventory", Constants.NBT.TAG_LIST) || stack.getTagCompound().getTagList("Inventory", stack.getTagCompound().getId()).tagCount() <= 0) {
            list.add(StringHelper.localize("info.LA.empty"));
            return;
        }
        NBTTagList nbtList = stack.getTagCompound().getTagList("Inventory", stack.getTagCompound().getId());
        ItemStack curStack;
        ItemStack curStack2;

        ArrayList<ItemStack> containedItems = new ArrayList<>();

        boolean[] visited = new boolean[nbtList.tagCount()];

        for (int i = 0; i < nbtList.tagCount(); i++) {
            NBTTagCompound tag = nbtList.getCompoundTagAt(i);
            int slot = tag.getInteger("Slot");

            if (visited[i] || slot < minSlot || slot > maxSlot) {
                continue;
            }
            visited[i] = true;
            curStack = new ItemStack(tag);

            if (curStack.isEmpty()) {
                continue;
            }
            containedItems.add(curStack);
            for (int j = 0; j < nbtList.tagCount(); j++) {
                NBTTagCompound tag2 = nbtList.getCompoundTagAt(j);
                int slot2 = tag.getInteger("Slot");

                if (visited[j] || slot2 < minSlot || slot2 > maxSlot) {
                    continue;
                }
                curStack2 = new ItemStack(tag2);

                if (curStack.isEmpty()) {
                    continue;
                }
                if (itemsIdentical(curStack, curStack2)) {
                    curStack.grow(curStack2.getCount());
                    visited[j] = true;
                }
            }
        }
        if (containedItems.size() > 0) {
            list.add(StringHelper.localize("info.LA.contents") + ":");
        }
        for (ItemStack item : containedItems) {
            int maxStackSize = item.getMaxStackSize();

            if (!StringHelper.displayStackCount || item.getCount() < maxStackSize || maxStackSize == 1) {
                list.add("    " + StringHelper.ORANGE + item.getCount() + " " + StringHelper.getItemName(item));
            } else {
                if (item.getCount() % maxStackSize != 0) {
                    list.add("    " + StringHelper.ORANGE + maxStackSize + "x" + item.getCount() / maxStackSize + "+" + item.getCount() % maxStackSize + " " + StringHelper.getItemName(item));
                } else {
                    list.add("    " + StringHelper.ORANGE + maxStackSize + "x" + item.getCount() / maxStackSize + " " + StringHelper.getItemName(item));
                }
            }
        }
    }

    public static void addAccessibleInventoryInformation(ItemStack stack, List<String> list, int minSlot, int maxSlot) {

        int invSize = ((IInventoryContainerItem) stack.getItem()).getSizeInventory(stack);
        ItemStack curStack;
        ItemStack curStack2;

        ArrayList<ItemStack> containedItems = new ArrayList<>();

        boolean[] visited = new boolean[invSize];

        NBTTagCompound tag = stack.getTagCompound();
        if (tag.hasKey("Inventory")) {
            tag = tag.getCompoundTag("Inventory");
        }
        for (int i = minSlot; i < Math.min(invSize, maxSlot); i++) {
            if (visited[i]) {
                continue;
            }
            if (!tag.hasKey("Slot" + i)) {
                continue;
            }
            curStack = new ItemStack(tag.getCompoundTag("Slot" + i));
            visited[i] = true;

            if (curStack.isEmpty()) {
                continue;
            }
            containedItems.add(curStack);
            for (int j = minSlot; j < Math.min(invSize, maxSlot); j++) {
                if (visited[j]) {
                    continue;
                }
                if (!tag.hasKey("Slot" + j)) {
                    continue;
                }
                curStack2 = new ItemStack(tag.getCompoundTag("Slot" + j));

                if (curStack2.isEmpty()) {
                    continue;
                }
                if (itemsIdentical(curStack, curStack2)) {
                    curStack.grow(curStack2.getCount());
                    visited[j] = true;
                }
            }
        }
        if (containedItems.size() > 0) {
            list.add(StringHelper.localize("info.LA.contents") + ":");
        } else {
            list.add(StringHelper.localize("info.LA.empty"));
        }
        for (ItemStack item : containedItems) {
            int maxStackSize = item.getMaxStackSize();

            if (!StringHelper.displayStackCount || item.getCount() < maxStackSize || maxStackSize == 1) {
                list.add("    " + StringHelper.ORANGE + item.getCount() + " " + StringHelper.getItemName(item));
            } else {
                if (item.getCount() % maxStackSize != 0) {
                    list.add("    " + StringHelper.ORANGE + maxStackSize + "x" + item.getCount() / maxStackSize + "+" + item.getCount() % maxStackSize + " " + StringHelper.getItemName(item));
                } else {
                    list.add("    " + StringHelper.ORANGE + maxStackSize + "x" + item.getCount() / maxStackSize + " " + StringHelper.getItemName(item));
                }
            }
        }
    }

    /**
     * Compares item, meta, size and nbt of two stacks while ignoring nbt tag keys provided.
     * This is useful in shouldCauseReequipAnimation overrides.
     *
     * @param stackA          first stack to compare
     * @param stackB          second stack to compare
     * @param nbtTagsToIgnore tag keys to ignore when comparing the stacks
     */
    public static boolean areItemStacksEqualIgnoreTags(ItemStack stackA, ItemStack stackB, String... nbtTagsToIgnore) {

        if (stackA.isEmpty() && stackB.isEmpty()) {
            return true;
        }
        if (stackA.isEmpty() && !stackB.isEmpty()) {
            return false;
        }
        if (!stackA.isEmpty() && stackB.isEmpty()) {
            return false;
        }
        if (stackA.getItem() != stackB.getItem()) {
            return false;
        }
        if (stackA.getItemDamage() != stackB.getItemDamage()) {
            return false;
        }
        if (stackA.getCount() != stackB.getCount()) {
            return false;
        }
        if (stackA.getTagCompound() == null && stackB.getTagCompound() == null) {
            return true;
        }
        if (stackA.getTagCompound() == null && stackB.getTagCompound() != null) {
            return false;
        }
        if (stackA.getTagCompound() != null && stackB.getTagCompound() == null) {
            return false;
        }
        int numberOfKeys = stackA.getTagCompound().getKeySet().size();
        if (numberOfKeys != stackB.getTagCompound().getKeySet().size()) {
            return false;
        }

        NBTTagCompound tagA = stackA.getTagCompound();
        NBTTagCompound tagB = stackB.getTagCompound();

        String[] keys = new String[numberOfKeys];
        keys = tagA.getKeySet().toArray(keys);

        a:
        for (int i = 0; i < numberOfKeys; i++) {
            for (int j = 0; j < nbtTagsToIgnore.length; j++) {
                if (nbtTagsToIgnore[j].equals(keys[i])) {
                    continue a;
                }
            }
            if (!tagA.getTag(keys[i]).equals(tagB.getTag(keys[i]))) {
                return false;
            }
        }
        return true;
    }
}
