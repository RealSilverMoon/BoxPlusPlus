package com.silvermoon.boxplusplus.network.packet;

import com.silvermoon.boxplusplus.common.loader.ServerInitLoader;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class ServerJoinedPacket implements IMessage, IMessageHandler<ServerJoinedPacket, IMessage> {

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    @Override
    public IMessage onMessage(ServerJoinedPacket message, MessageContext ctx) {
        ServerInitLoader.initOnPlayerJoinedSever();
        return null;
    }
}
