package com.stormy.lightninglib.lib.item;

import cofh.item.IMultiModeItem;
import com.stormy.lightningadditions.creativetab.CreativeTabLA;
import com.stormy.lightninglib.lib.utils.StringHelper;
import net.minecraft.item.ItemStack;

import java.util.List;

public class LAProps {

    private LAProps() {}

    public static void preInit() {

        configCommon();
        configClient(); }

    public static void loadComplete() { }

    /* HELPERS */
    private static void configCommon() { }

    private static void configClient() { }

    public static void addEnhancedTip(IMultiModeItem item, ItemStack stack, List<String> tooltip) {

        if (item.getMode(stack) == 1) {
            tooltip.add(StringHelper.localizeFormat("info.LA.tool.chargeOff"));
        } else {
            tooltip.add(StringHelper.localizeFormat("info.LA.tool.chargeOn"));
        }
    }

    /* INTERFACE */
    public static boolean showArmorCharge = true;
    public static boolean showToolCharge = true;

}
