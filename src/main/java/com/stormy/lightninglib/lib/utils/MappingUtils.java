package com.stormy.lightninglib.lib.utils;

import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MappingUtils {

    @SideOnly(Side.CLIENT)
    public static Minecraft mc() { return Minecraft.getMinecraft(); }

    @SideOnly(Side.CLIENT)
    public static EntityPlayer player() { return mc().player; }

    @SideOnly(Side.CLIENT)
    public static World world() { return mc().world; }

    public static World world(Entity entity) { return entity.world; }

    public static int slotPosX(Slot slot) { return slot.xPos; }

    public static int slotPosY(Slot slot) { return slot.yPos; }

    public static boolean spawn(World world, Entity entity) { return world.spawnEntity(entity); }

    public static NBTTagCompound readNBT(PacketBuffer buf)
    { try { return buf.readCompoundTag(); }
        catch (IOException e) { }
        return null; }

    public static PacketBuffer writeNBT(@Nullable NBTTagCompound nbt, PacketBuffer buf)
    { return buf.writeCompoundTag(nbt); }

    public static String[] getNames(MinecraftServer server)
    { return server.getOnlinePlayerNames(); }

}
