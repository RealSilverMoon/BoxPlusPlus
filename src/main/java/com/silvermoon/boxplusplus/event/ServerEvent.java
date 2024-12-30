package com.silvermoon.boxplusplus.event;

import static com.silvermoon.boxplusplus.boxplusplus.LOG;

import net.minecraft.entity.player.EntityPlayerMP;

import com.silvermoon.boxplusplus.network.NetworkLoader;
import com.silvermoon.boxplusplus.network.packet.ServerJoinedPacket;

import bartworks.API.SideReference;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public class ServerEvent {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void PlayerJoinServerEvent(PlayerEvent.PlayerLoggedInEvent event) {
        LOG.info("PlayerJoinServerEvent running");
        if (event == null || !(event.player instanceof EntityPlayerMP player) || !SideReference.Side.Server) return;
        NetworkLoader.instance.sendTo(new ServerJoinedPacket(), player);
        LOG.info("PlayerJoinServerEvent run finished");
    }
}
