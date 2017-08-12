package com.stormy.lightninglib.lib.utils;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import javax.annotation.Nonnull;

public class ItemstackUtils {
    private ItemstackUtils() {
    }

    public static boolean isSmeltable(ItemStack itemStack)
    { return isValid(itemStack) && isValid(FurnaceRecipes.instance().getSmeltingResult(itemStack)); }

    public static boolean isItemInstanceOf(ItemStack itemStack, Class itemClass)
    { return isValid(itemStack) && itemClass != null && itemClass.isInstance(itemStack.getItem()); }

    public static boolean doItemsMatch(ItemStack itemStack, Item item)
    { return isValid(itemStack) && itemStack.getItem() == item; }

    public static boolean isValid(ItemStack itemStack)
    { return !itemStack.isEmpty(); }

    @Nonnull
    public static String getModIdFromItemStack(@Nonnull ItemStack itemStack)
    { String modid = "";
        Item item = itemStack.getItem();
        if (item.getRegistryName() != null)
        { modid = item.getRegistryName().getResourceDomain(); }
        return modid; }
}
