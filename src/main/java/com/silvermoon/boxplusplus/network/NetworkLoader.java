package com.silvermoon.boxplusplus.network;

import com.silvermoon.boxplusplus.Tags;
import com.silvermoon.boxplusplus.network.packet.ServerJoinedPacket;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class NetworkLoader {

    public static SimpleNetworkWrapper instance = NetworkRegistry.INSTANCE.newSimpleChannel(Tags.MODID);

    private static int nextID = 0;

    public static void init() {
        instance.registerMessage(MessageRouting.Handler.class, MessageRouting.class, nextID++, Side.SERVER);

        instance.registerMessage(ServerJoinedPacket.class, ServerJoinedPacket.class, nextID++, Side.SERVER);
        instance.registerMessage(ServerJoinedPacket.class, ServerJoinedPacket.class, nextID++, Side.CLIENT);
    }
}
