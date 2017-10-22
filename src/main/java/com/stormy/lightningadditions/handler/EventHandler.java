package com.stormy.lightningadditions.handler;

import com.stormy.lightningadditions.reference.ModInformation;
import com.stormy.lightningadditions.tile.resource.PowerManager;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = ModInformation.MODID)
public class EventHandler
{

    public static void init(FMLInitializationEvent event)
    {
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent event)
    {
        if (event.phase == Phase.END)
            return;
        if (event.side == Side.CLIENT)
            return;
        manager.updatePlayer((EntityPlayerMP) event.player);
    }

    public static void onServerStopping(FMLServerStoppingEvent event)
    {
        manager.clear(); }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent event)
    {
        if (event.phase == Phase.END)
            return;
        manager.updateChargers(); }

    public static void postInit(FMLPostInitializationEvent event)
    { }

    public static void preInit(FMLPreInitializationEvent event){}



    public static final PowerManager manager = new PowerManager();

}

