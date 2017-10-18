/*
 *
 *  * ********************************************************************************
 *  * Copyright (c) 2017 StormyMode, MiningMark48. All Rights Reserved!
 *  * This file is part of Lightning Additions (MC-Mod).
 *  *
 *  * This project cannot be copied and/or distributed without the express
 *  * permission of StormyMode, MiningMark48 (Developers)!
 *  * ********************************************************************************
 *
 */

package com.stormy.lightningadditions.handler.fatality;

import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.stormy.lightninglib.lib.utils.StringHelper.BOLD;

public class FatalityEventHandler
{

    @SubscribeEvent
    public void onWordGenerateSpawnPoint( WorldEvent.Load event)
    {
        World world = event.getWorld();
        ScoreObjective objective = world.getScoreboard().getObjective("deaths");
        ScoreObjective objective1 = world.getScoreboard().getObjective("kills");
        if (objective == null)
        {
            objective = world.getScoreboard().addScoreObjective("deaths", IScoreCriteria.DEATH_COUNT);
            objective.setDisplayName(TextFormatting.LIGHT_PURPLE + "Fatalities");
            world.getScoreboard().setObjectiveInDisplaySlot(0, objective); }

        if (objective1 == null)
        {
            objective = world.getScoreboard().addScoreObjective("kills", IScoreCriteria.PLAYER_KILL_COUNT);
            objective.setDisplayName(BOLD + TextFormatting.RED + "Experience");
            world.getScoreboard().setObjectiveInDisplaySlot(1, objective1); }
    }

    @SubscribeEvent
    public void onWordGenerateSpawnPoint( WorldEvent.CreateSpawnPosition event)
    {
        World world = event.getWorld();
        ScoreObjective objective = world.getScoreboard().addScoreObjective("deaths", IScoreCriteria.DEATH_COUNT);
        ScoreObjective objective1 = world.getScoreboard().addScoreObjective("kills", IScoreCriteria.PLAYER_KILL_COUNT);
        objective.setDisplayName(TextFormatting.LIGHT_PURPLE + "Fatalities");
        objective.setDisplayName(BOLD + TextFormatting.RED + "Experience");
        world.getScoreboard().setObjectiveInDisplaySlot(0, objective);
        world.getScoreboard().setObjectiveInDisplaySlot(1, objective1); }



}
