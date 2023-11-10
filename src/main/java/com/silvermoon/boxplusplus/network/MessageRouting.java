package com.silvermoon.boxplusplus.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import com.silvermoon.boxplusplus.common.tileentities.GTMachineBox;
import com.silvermoon.boxplusplus.util.BoxRoutings;
import com.silvermoon.boxplusplus.util.Util;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import gregtech.api.gui.modularui.GT_UIInfos;
import io.netty.buffer.ByteBuf;

public class MessageRouting implements IMessage {

    NBTTagCompound nbt;
    String uuid;

    // It's needed.
    public MessageRouting() {}

    @Override
    public void fromBytes(ByteBuf buf) {
        uuid = ByteBufUtils.readUTF8String(buf);
        nbt = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, uuid);
        ByteBufUtils.writeTag(buf, nbt);
    }

    public MessageRouting(NBTTagCompound nbt, EntityPlayer player) {
        this.nbt = nbt;
        this.uuid = player.getUniqueID()
            .toString();
    }

    public static class Handler implements IMessageHandler<MessageRouting, IMessage> {

        @Override
        public IMessage onMessage(MessageRouting message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                EntityPlayer player = Util.getPlayerFromUUID(message.uuid);
                if (player != null) {
                    GTMachineBox box = Util.boxMap.get(player);
                    box.routingMap.add(new BoxRoutings(message.nbt));
                    player.openContainer.detectAndSendChanges();
                    GT_UIInfos.openGTTileEntityUI(box.getBaseMetaTileEntity(), player);
                }
            }
            return null;
        }
    }
}
