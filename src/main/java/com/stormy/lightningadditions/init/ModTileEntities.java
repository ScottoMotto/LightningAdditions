/*
 * ********************************************************************************
 * Copyright (c) 2017 StormyMode, MiningMark48. All Rights Reserved!
 * This file is part of Lightning Additions (MC-Mod).
 *
 * This project cannot be copied and/or distributed without the express
 * permission of StormyMode, MiningMark48 (Developers)!
 * ********************************************************************************
 */

package com.stormy.lightningadditions.init;

import com.stormy.lightningadditions.tile.TileEntityPlacer;
import com.stormy.lightningadditions.tile.TileEnderHopper;
import com.stormy.lightningadditions.tile.TileEntitySharingXP;
import com.stormy.lightningadditions.tile.TileSoundMuffler;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class ModTileEntities {

    public static void init()
    {
        GameRegistry.registerTileEntity(TileEntitySharingXP.class, "share_xp");
        GameRegistry.registerTileEntity(TileSoundMuffler.class, "noise_muffler");
        GameRegistry.registerTileEntity(TileEntityPlacer.class, "placer");
        GameRegistry.registerTileEntity(TileEnderHopper.class, "ender_hopper");
    }

}
