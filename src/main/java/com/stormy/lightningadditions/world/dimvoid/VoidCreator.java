/*
 * ********************************************************************************
 * Copyright (c) 2017 StormyMode, MiningMark48. All Rights Reserved!
 * This file is part of Lightning Additions (MC-Mod).
 *
 * This project cannot be copied and/or distributed without the express
 * permission of StormyMode, MiningMark48 (Developers)!
 * ********************************************************************************
 */

package com.stormy.lightningadditions.world.dimvoid;

import com.stormy.lightningadditions.LightningAdditions;
import com.stormy.lightningadditions.utility.logger.ConfigurationManagerLA;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkGenerator;

import javax.annotation.Nullable;

public class VoidCreator extends WorldProvider {
    @Override
    public DimensionType getDimensionType()
    { return LightningAdditions.DimType; }

    @Override
    public IChunkGenerator createChunkGenerator()
    { return new VoidChunks(world); }

    @Override
    public boolean canRespawnHere()
    { return true; }

    @Override
    public Vec3d getFogColor(float p_76562_1_, float p_76562_2_)
    {
        if (ConfigurationManagerLA.nightPhase)
        { return new Vec3d(0D, 0D, 0D); }
          return super.getFogColor(p_76562_1_, p_76562_2_);
    }

    @Override
    public Vec3d getSkyColor(Entity cameraEntity, float partialTicks)
    {
        if (ConfigurationManagerLA.nightPhase)
        { return new Vec3d(0D, 0D, 0D); }
          return super.getSkyColor(cameraEntity, partialTicks);
    }

    @Override
    public long getWorldTime()
    {
        if (ConfigurationManagerLA.zenithPhase)
        { return 6000; }
        return super.getWorldTime();
    }

    @Override
    public boolean isDaytime()
    {
        if (ConfigurationManagerLA.zenithPhase)
        { return true; }
        return super.isDaytime();
    }

    @Nullable
    @Override
    public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks)
    {
        if (ConfigurationManagerLA.nightPhase)
        { return null; }
        return super.calcSunriseSunsetColors(celestialAngle, partialTicks);
    }
}
