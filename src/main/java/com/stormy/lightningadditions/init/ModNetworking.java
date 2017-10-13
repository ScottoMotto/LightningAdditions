package com.stormy.lightningadditions.init;

import com.stormy.lightningadditions.network.messages.MessageDisplayCase;
import com.stormy.lightningadditions.reference.ModInformation;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class ModNetworking {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ModInformation.MODID);

    public static void init(){
        INSTANCE.registerMessage(MessageDisplayCase.class, MessageDisplayCase.class, 10, Side.SERVER);
    }

}
